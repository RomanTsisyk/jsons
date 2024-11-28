package io.github.romantsisyk.cryptolib.crypto.qr

import android.graphics.Bitmap
import org.junit.Assert.*
import org.junit.Test
import org.robolectric.RobolectricTestRunner
import org.junit.runner.RunWith

@RunWith(RobolectricTestRunner::class)
class BitmapLuminanceSourceTest {

    @Test
    fun testGetMatrix() {
        val bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888)
        bitmap.setPixel(0, 0, 0xFF000000.toInt()) // Black
        bitmap.setPixel(0, 1, 0xFFFFFFFF.toInt()) // White

        val source = BitmapLuminanceSource(bitmap)
        val matrix = source.matrix
        assertEquals(4, matrix.size)
    }
}