package io.github.romantsisyk.cryptolib.biometrics


import io.github.romantsisyk.cryptolib.crypto.keymanagement.KeyHelper
import javax.crypto.Cipher
import io.github.romantsisyk.cryptolib.crypto.aes.AESEncryption
import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity

class BiometricHelper(private val context: Context) {

    fun authenticate(
        activity: FragmentActivity,
        title: String,
        description: String,
        encryptedData: ByteArray,
        onSuccess: (ByteArray) -> Unit,
        onError: (Exception) -> Unit,
        onAuthenticationError: (Int, CharSequence) -> Unit
    ) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setDescription(description)
            .setSubtitle("Log in using your biometrics")
            .setDescription("Place your fingerprint on the sensor to authenticate")
            .setNegativeButtonText("Cancel")
            .build()

        val biometricPrompt = BiometricPrompt(
            activity,
            activity.mainExecutor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    try {
                        val decryptedData = result.cryptoObject?.cipher?.let {
                            AESEncryption.decrypt(
                                encryptedData.toString(Charsets.UTF_8),
                                KeyHelper.getKey()
                            )
                        }
                        if (decryptedData != null) {
                            onSuccess(decryptedData)
                        } else {
                            onError(Exception("Decryption returned null"))
                        }
                    } catch (e: Exception) {
                        onError(e)
                    }
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onAuthenticationError(errorCode, errString)
                    onError(Exception("Authentication error [$errorCode]: $errString"))

                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(context, "Authentication failed. Try again.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        )

        val cipher = getCipher()
        val cryptoObject = BiometricPrompt.CryptoObject(cipher)

        biometricPrompt.authenticate(promptInfo, cryptoObject)
    }

    private fun getCipher(): Cipher {
        return try {
            val secretKey = KeyHelper.getKey() // Retrieve the secure key
            val cipher = KeyHelper.getCipherInstance() // Get Cipher instance
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            cipher
        } catch (e: Exception) {
            throw IllegalStateException("Failed to initialize Cipher", e)
        }
    }
}