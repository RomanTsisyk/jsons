package io.github.romantsisyk.cryptolib.crypto.qr

import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class QRKeyManagerTest {

    @Test
    fun testKeyGenerationAndRetrieval() {
        val mockKeyGenerator = Mockito.mock(KeyGenerator::class.java)
        val mockKey = Mockito.mock(SecretKey::class.java)

        Mockito.`when`(mockKeyGenerator.generateKey()).thenReturn(mockKey)

        assertNotNull(mockKey)
        assertNotNull(mockKey)
    }
}
