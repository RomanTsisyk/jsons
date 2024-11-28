package io.github.romantsisyk.cryptolib.qr

import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.romantsisyk.cryptolib.crypto.qr.QRCodeGenerator
import io.github.romantsisyk.cryptolib.crypto.qr.QRCodeScanner
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QRCodeScannerTest {

    @Test
    fun testDecodeQRCodeValid() {
        val bitmap = QRCodeGenerator.generateQRCode("Valid QR Code")
        val result = QRCodeScanner.decodeQRCode(bitmap)
        assertEquals("Valid QR Code", result)
    }

    @Test
    fun testDecodeQRCodeInvalid() {
        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val result = QRCodeScanner.decodeQRCode(bitmap)
        assertNull(result)
    }
}
