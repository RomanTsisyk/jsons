package io.github.romantsisyk.cryptolib

import android.app.Activity
import android.content.Context
import io.github.romantsisyk.cryptolib.exceptions.AuthenticationException
import io.github.romantsisyk.cryptolib.exceptions.CryptoLibException
import io.github.romantsisyk.cryptolib.exceptions.CryptoOperationException
import io.github.romantsisyk.cryptolib.exceptions.KeyNotFoundException

object CryptoManager {

    /**
     * Encrypts data after authenticating the user if required.
     */
    fun encryptData(
        activity: Activity,
        context: Context,
        config: CryptoConfig,
        plaintext: ByteArray,
        onSuccess: (String) -> Unit,
        onFailure: (CryptoLibException) -> Unit
    ) {
        try {
            // Check if key exists, else generate
            val keyExists = KeyHelper.listKeys().contains(config.keyAlias)
            if (!keyExists) {
                KeyHelper.generateAESKey(
                    alias = config.keyAlias,
                    validityDays = config.keyValidityDays,
                    requireUserAuthentication = config.requireUserAuthentication
                )
            }

            val secretKey = KeyHelper.getAESKey(config.keyAlias)

            if (config.requireUserAuthentication) {
                BiometricHelper.authenticateWithFallback(
                    activity,
                    title = "Encrypt Data",
                    subtitle = "Authenticate to encrypt your data",
                    description = "Use your biometric credential or device PIN to proceed",
                    onSuccess = {
                        try {
                            val encryptedData = AESEncryption.encrypt(plaintext, secretKey)
                            onSuccess(encryptedData)
                        } catch (e: CryptoOperationException) {
                            onFailure(e)
                        }
                    },
                    onFailure = { exception ->
                        onFailure(AuthenticationException(exception?.message ?: "Unknown error"))
                    }
                )
            } else {
                val encryptedData = AESEncryption.encrypt(plaintext, secretKey)
                onSuccess(encryptedData)
            }

            // Schedule key rotation
            KeyRotationManager.rotateKeyIfNeeded(config.keyAlias)

        } catch (e: KeyNotFoundException) {
            onFailure(e)
        } catch (e: CryptoLibException) {
            onFailure(e)
        }
    }

    /**
     * Decrypts data after authenticating the user if required.
     */
    fun decryptData(
        activity: Activity,
        context: Context,
        config: CryptoConfig,
        encryptedData: String,
        onSuccess: (ByteArray) -> Unit,
        onFailure: (CryptoLibException) -> Unit
    ) {
        try {
            val secretKey = KeyHelper.getAESKey(config.keyAlias)

            if (config.requireUserAuthentication) {
                BiometricHelper.authenticateWithFallback(
                    activity,
                    title = "Decrypt Data",
                    subtitle = "Authenticate to decrypt your data",
                    description = "Use your biometric credential or device PIN to proceed",
                    onSuccess = {
                        try {
                            val decryptedData = AESEncryption.decrypt(encryptedData, secretKey)
                            onSuccess(decryptedData)
                        } catch (e: CryptoOperationException) {
                            onFailure(e)
                        }
                    },
                    onFailure = { exception ->
                        onFailure(AuthenticationException(exception?.message ?: "Unknown error"))
                    }
                )
            } else {
                val decryptedData = AESEncryption.decrypt(encryptedData, secretKey)
                onSuccess(decryptedData)
            }

        } catch (e: KeyNotFoundException) {
            onFailure(e)
        } catch (e: CryptoLibException) {
            onFailure(e)
        }
    }
}
