package io.github.romantsisyk.cryptolib.crypto.aes

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class AESEncryptionTest {

    @Test
    fun `test AES encryption and decryption`() {
        val key = AESEncryption.generateKey()
        val originalText = "Hello, World!".toByteArray()
        val encryptedText = AESEncryption.encrypt(originalText, key)
        val decryptedText = AESEncryption.decrypt(encryptedText, key)

        assertArrayEquals(originalText, decryptedText)
    }

    @Test
    fun `test AES decryption with invalid key`() {
        val key = AESEncryption.generateKey()
        val invalidKey = AESEncryption.generateKey()
        val originalText = "Test".toByteArray()
        val encryptedText = AESEncryption.encrypt(originalText, key)

        assertThrows(Exception::class.java) {
            AESEncryption.decrypt(encryptedText, invalidKey)
        }
    }
}
