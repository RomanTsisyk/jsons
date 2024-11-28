package io.github.romantsisyk.cryptolib.qr

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.romantsisyk.cryptolib.crypto.qr.QRKeyManager
import io.github.romantsisyk.cryptolib.crypto.qr.QRUtils
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QRUtilsTest {

    @Test
    fun testEncryptDecryptWithRealKey() {
        val key = QRKeyManager.generateKey()
        assertNotNull(key)

        val data = "Test Data"
        val (encryptedData, iv) = QRUtils.encryptData(data, key)
        assertNotNull(encryptedData)
        assertNotNull(iv)

        val decryptedData = QRUtils.decryptData(encryptedData, key, iv)
        assertEquals(data, decryptedData)
    }
}
