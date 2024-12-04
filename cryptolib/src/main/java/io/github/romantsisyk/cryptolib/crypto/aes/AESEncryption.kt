package io.github.romantsisyk.cryptolib.crypto.aes

import io.github.romantsisyk.cryptolib.crypto.CryptoConstants.AES_KEY_SIZE
import io.github.romantsisyk.cryptolib.crypto.CryptoConstants.AES_TRANSFORMATION
import io.github.romantsisyk.cryptolib.crypto.CryptoConstants.ERROR_DECRYPTION
import io.github.romantsisyk.cryptolib.crypto.CryptoConstants.ERROR_ENCRYPTION
import io.github.romantsisyk.cryptolib.crypto.CryptoConstants.GCM_IV_SIZE
import io.github.romantsisyk.cryptolib.crypto.CryptoConstants.GCM_TAG_SIZE
import java.util.Base64
import io.github.romantsisyk.cryptolib.exceptions.CryptoOperationException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object AESEncryption {

    /**
     * Encrypts plaintext using AES-GCM.
     * Throws CryptoOperationException on failure.
     */
    fun encrypt(plaintext: ByteArray, key: SecretKey): String {
        return try {
            val cipher = Cipher.getInstance(AES_TRANSFORMATION)

            // Generate a random IV
            val iv = ByteArray(GCM_IV_SIZE)
            SecureRandom().nextBytes(iv)
            val spec = GCMParameterSpec(GCM_TAG_SIZE, iv)

            cipher.init(Cipher.ENCRYPT_MODE, key, spec)
            val ciphertext = cipher.doFinal(plaintext)

            // Prepend IV to ciphertext
            val encrypted = iv + ciphertext

            // Return as Base64 string
            Base64.getEncoder().encodeToString(encrypted)
        } catch (e: Exception) {
            throw CryptoOperationException(ERROR_ENCRYPTION, e)
        }
    }

    /**
     * Decrypts ciphertext using AES-GCM.
     * Throws CryptoOperationException on failure.
     */
    fun decrypt(encryptedData: String, key: SecretKey): ByteArray {
        return try {
            val encryptedBytes = Base64.getDecoder().decode(encryptedData)

            // Extract IV and ciphertext
            val iv = encryptedBytes.copyOfRange(0, GCM_IV_SIZE)
            val ciphertext = encryptedBytes.copyOfRange(GCM_IV_SIZE, encryptedBytes.size)
            val spec = GCMParameterSpec(GCM_IV_SIZE, iv)

            val cipher = Cipher.getInstance(AES_TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, key, spec)
            cipher.doFinal(ciphertext)
        } catch (e: Exception) {
            throw CryptoOperationException(ERROR_DECRYPTION, e)
        }
    }

    /**
     * Generates a new AES key.
     */
    fun generateKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(AES_KEY_SIZE)
        return keyGenerator.generateKey()
    }
}
