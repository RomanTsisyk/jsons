package io.github.romantsisyk.cryptolib.crypto.aes

import java.util.Base64
import io.github.romantsisyk.cryptolib.exceptions.CryptoOperationException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object AESEncryption {

    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val IV_SIZE = 12 // Recommended IV size for GCM
    private const val TAG_SIZE = 128
    private const val KEY_SIZE = 256

    /**
     * Encrypts plaintext using AES-GCM.
     * Throws CryptoOperationException on failure.
     */
    fun encrypt(plaintext: ByteArray, key: SecretKey): String {
        return try {
            val cipher = Cipher.getInstance(TRANSFORMATION)

            // Generate a random IV
            val iv = ByteArray(IV_SIZE)
            SecureRandom().nextBytes(iv)
            val spec = GCMParameterSpec(TAG_SIZE, iv)

            cipher.init(Cipher.ENCRYPT_MODE, key, spec)
            val ciphertext = cipher.doFinal(plaintext)

            // Prepend IV to ciphertext
            val encrypted = iv + ciphertext

            // Return as Base64 string
            Base64.getEncoder().encodeToString(encrypted)
        } catch (e: Exception) {
            throw CryptoOperationException("Encryption", e)
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
            val iv = encryptedBytes.copyOfRange(0, IV_SIZE)
            val ciphertext = encryptedBytes.copyOfRange(IV_SIZE, encryptedBytes.size)
            val spec = GCMParameterSpec(TAG_SIZE, iv)

            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, key, spec)
            cipher.doFinal(ciphertext)
        } catch (e: Exception) {
            throw CryptoOperationException("Decryption", e)
        }
    }

    /**
     * Generates a new AES key.
     */
    fun generateKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(KEY_SIZE)
        return keyGenerator.generateKey()
    }
}
