package io.github.romantsisyk.cryptolib.crypto.qr

import android.graphics.Bitmap
import com.google.zxing.LuminanceSource

class BitmapLuminanceSource(private val bitmap: Bitmap) : LuminanceSource(bitmap.width, bitmap.height) {

    private val luminanceArray: ByteArray

    init {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        luminanceArray = ByteArray(width * height)
        for (i in pixels.indices) {
            val pixel = pixels[i]

            val alpha = (pixel shr 24) and 0xff
            val red = (pixel shr 16) and 0xff
            val green = (pixel shr 8) and 0xff
            val blue = pixel and 0xff

            val adjustedRed = red * alpha / 255
            val adjustedGreen = green * alpha / 255
            val adjustedBlue = blue * alpha / 255

            luminanceArray[i] = (0.299 * adjustedRed + 0.587 * adjustedGreen + 0.114 * adjustedBlue).toInt().toByte()
        }
    }

    override fun getRow(y: Int, row: ByteArray?): ByteArray {
        val width = width
        val rowArray = row ?: ByteArray(width)
        System.arraycopy(luminanceArray, y * width, rowArray, 0, width)
        return rowArray
    }

    override fun getMatrix(): ByteArray = luminanceArray
}