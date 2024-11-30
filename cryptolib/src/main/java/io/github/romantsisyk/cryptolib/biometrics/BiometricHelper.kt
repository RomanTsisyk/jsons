package io.github.romantsisyk.cryptolib.biometrics

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import io.github.romantsisyk.cryptolib.crypto.keymanagement.KeyHelper
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * A helper class for managing biometric authentication with secure cryptographic operations.
 */
class BiometricHelper(
    private val activity: FragmentActivity,
    private val executor: java.util.concurrent.Executor,
    private val onSuccess: (decryptedData: ByteArray?) -> Unit,
    private val onFailure: (error: String) -> Unit
) {

    private val keyAlias = "MySecretKey"

    init {
        generateSecretKey()
    }

    private fun generateSecretKey() {
        try {
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                keyAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setUserAuthenticationRequired(true)
                .build()

            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                "AndroidKeyStore"
            )
            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        } catch (e: Exception) {
            onFailure("Failed to generate secure key: ${e.message}")
        }
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        return keyStore.getKey(keyAlias, null) as SecretKey
    }

    private fun getCipher(): Cipher {
        return Cipher.getInstance(
            "${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_CBC}/${KeyProperties.ENCRYPTION_PADDING_PKCS7}"
        )
    }

    fun authenticateWithBiometrics(encryptedData: ByteArray) {
        try {
            val cipher = getCipher()
            val secretKey = getSecretKey()
            cipher.init(Cipher.DECRYPT_MODE, secretKey)

            val biometricPrompt = BiometricPrompt(
                activity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        val decryptedData = result.cryptoObject?.cipher?.doFinal(encryptedData)
                        onSuccess(decryptedData)
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        onFailure("Authentication error: $errString")
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        onFailure("Authentication failed")
                    }
                }
            )

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Authenticate to access secure data")
                .setNegativeButtonText("Cancel")
                .build()

            biometricPrompt.authenticate(
                promptInfo,
                BiometricPrompt.CryptoObject(cipher)
            )
        } catch (e: Exception) {
            onFailure("Biometric authentication failed: ${e.message}")
        }
    }

    companion object {

        /**
         * Handles biometric authentication with simplified usage.
         */
        fun authenticate(
            activity: FragmentActivity,
            title: String,
            description: String,
            onSuccess: (Cipher) -> Unit,
            onFailure: (Exception?) -> Unit
        ) {
            try {
                val executor = activity.mainExecutor

                val cipher = Cipher.getInstance(
                    "${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_GCM}/${KeyProperties.ENCRYPTION_PADDING_NONE}"
                )
                val secretKey = KeyHelper.getOrCreateSecretKey()
                cipher.init(Cipher.ENCRYPT_MODE, secretKey)

                val biometricPrompt = BiometricPrompt(
                    activity,
                    executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            onSuccess(cipher)
                        }

                        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                            super.onAuthenticationError(errorCode, errString)
                            onFailure(Exception(errString.toString()))
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            onFailure(Exception("Authentication failed"))
                        }
                    }
                )

                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle(title)
                    .setDescription(description)
                    .setNegativeButtonText("Cancel")
                    .build()

                biometricPrompt.authenticate(
                    promptInfo,
                    BiometricPrompt.CryptoObject(cipher)
                )
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }
}