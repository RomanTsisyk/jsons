package io.github.romantsisyk.cryptolib.crypto.qr

import android.graphics.Bitmap
import com.google.zxing.BinaryBitmap
import com.google.zxing.Reader
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader

object QRCodeScanner {

    fun decodeQRCode(bitmap: Bitmap): String? {
        val source = BitmapLuminanceSource(bitmap)
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
        val qrCodeReader: Reader = QRCodeReader()

        return try {
            qrCodeReader.decode(binaryBitmap).text
        } catch (e: Exception) {
            null // Return null if decoding fails
        }
    }
}