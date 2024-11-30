package io.github.romantsisyk.cryptolib.keymanagement

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.romantsisyk.cryptolib.crypto.keymanagement.KeyHelper
import io.github.romantsisyk.cryptolib.exceptions.KeyNotFoundException
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class KeyHelperIntegrationTest {

    private val alias = "integrationTestAlias"

    @After
    fun tearDown() {
        try {
            KeyHelper.deleteKey(alias)
        } catch (e: Exception) {
            // Ignore cleanup errors
        }
    }

    @Test
    fun testFullKeyLifecycle() {
        KeyHelper.generateAESKey(alias)
        val generatedKey = KeyHelper.getAESKey(alias)
        assertNotNull("Generated key should not be null", generatedKey)

        val retrievedKey = KeyHelper.getAESKey(alias)
        assertEquals("Generated key and retrieved key should match", generatedKey, retrievedKey)

        val keys = KeyHelper.listKeys()
        assertTrue("Generated key alias should appear in key list", keys.contains(alias))

        KeyHelper.deleteKey(alias)
        val remainingKeys = KeyHelper.listKeys()
        assertFalse("Deleted key alias should not appear in key list", remainingKeys.contains(alias))
    }

    @Test(expected = KeyNotFoundException::class)
    fun testRetrieveNonexistentKey() {
        KeyHelper.getAESKey("nonexistentAlias")
    }
}