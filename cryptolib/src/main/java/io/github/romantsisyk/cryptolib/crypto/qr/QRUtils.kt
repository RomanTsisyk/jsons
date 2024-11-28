package io.github.romantsisyk.cryptolib.crypto.qr

import javax.crypto.Cipher
import javax.crypto.SecretKey
import android.util.Base64
import javax.crypto.spec.GCMParameterSpec


object QRUtils {
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val TAG_LENGTH_BIT = 128

    fun encryptData(data: String, key: SecretKey): Pair<String, ByteArray> {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv // Initialization vector
        val encryptedBytes = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT) to iv
    }

    fun decryptData(encryptedData: String, key: SecretKey, iv: ByteArray): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val gcmSpec = GCMParameterSpec(TAG_LENGTH_BIT, iv)
        cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec)
        val decodedBytes = Base64.decode(encryptedData, Base64.DEFAULT)
        val decryptedBytes = cipher.doFinal(decodedBytes)
        return String(decryptedBytes)
    }
}
