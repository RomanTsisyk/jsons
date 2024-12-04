package io.github.romantsisyk.cryptolib.crypto.digital

import org.junit.Assert.*
import org.junit.Test

class DigitalSignatureTest {

    @Test
    fun `test digital signature verification`() {
        val keyPair = DigitalSignature.generateKeyPair()
        val message = "This is a signed message.".toByteArray()
        val signature = DigitalSignature.sign(message, keyPair.private)

        assertTrue(DigitalSignature.verify(message, signature, keyPair.public))
    }

    @Test
    fun `test digital signature verification with tampered message`() {
        val keyPair = DigitalSignature.generateKeyPair()
        val message = "Original message.".toByteArray()
        val tamperedMessage = "Tampered message.".toByteArray()
        val signature = DigitalSignature.sign(message, keyPair.private)

        assertFalse(DigitalSignature.verify(tamperedMessage, signature, keyPair.public))
    }
}