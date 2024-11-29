package io.github.romantsisyk.cryptolib.keymanagement

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.romantsisyk.cryptolib.crypto.keymanagement.KeyHelper
import io.github.romantsisyk.cryptolib.crypto.keymanagement.KeyRotationManager
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class KeyRotationIntegrationTest {

    @Test
    fun testFullKeyRotationFlow() {
        val alias = "testAlias"

        KeyHelper.generateAESKey(alias, validityDays = -1)

        val needsRotation = KeyRotationManager.isKeyRotationNeeded(alias)
        assertTrue("Key should require rotation", needsRotation)

        KeyRotationManager.rotateKeyIfNeeded(alias)

        val keys = KeyHelper.listKeys()
        assertTrue("New key alias should exist", keys.contains(alias))
    }
}