package io.github.romantsisyk.cryptolib.qr

import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.romantsisyk.cryptolib.crypto.qr.QRCodeGenerator
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QRCodeGeneratorTest {

    @Test
    fun testGenerateQRCode() {
        val bitmap: Bitmap = QRCodeGenerator.generateQRCode("Test")
        assertNotNull(bitmap)
        assertEquals(300, bitmap.width) // Ensure the QR code is 300x300 pixels
        assertEquals(300, bitmap.height)
    }
}