package io.github.romantsisyk.cryptolib.crypto.rsa

import org.junit.Assert.*
import org.junit.Test

class RSAEncryptionTest {

    @Test
    fun `test RSA encryption and decryption`() {
        val keyPair = RSAEncryption.generateKeyPair()
        val originalText = "Secure Message".toByteArray()
        val encryptedText = RSAEncryption.encrypt(originalText, keyPair.public)
        val decryptedText = RSAEncryption.decrypt(encryptedText, keyPair.private)

        assertArrayEquals(originalText, decryptedText)
    }

    @Test(expected = Exception::class)
    fun `test RSA decryption with invalid key`() {
        val keyPair = RSAEncryption.generateKeyPair()
        val invalidKeyPair = RSAEncryption.generateKeyPair()
        val originalText = "Secure Data".toByteArray()
        val encryptedText = RSAEncryption.encrypt(originalText, keyPair.public)

        RSAEncryption.decrypt(encryptedText, invalidKeyPair.private)
    }
}