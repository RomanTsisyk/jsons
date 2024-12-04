package io.github.romantsisyk.cryptolib.crypto.keymanagement

import android.util.Log
import io.github.romantsisyk.cryptolib.crypto.CryptoConstants
import io.github.romantsisyk.cryptolib.crypto.CryptoConstants.TAG_KEY_ROTATION
import java.util.Calendar
import java.util.Date

object KeyRotationManager {

    /**
     * Checks if the key needs rotation based on the rotation interval.
     * If rotation is needed, generates a new key and deletes the old one.
     */
    fun rotateKeyIfNeeded(alias: String) {
        val keyInfo = KeyHelper.getKeyInfo(alias)
        val keyValidityEndDate = keyInfo.keyValidityForOriginationEnd ?: return
        val currentDate = Date()

        if (currentDate.after(keyValidityEndDate)) {
            rotateKey(alias, keyValidityEndDate)
        } else {
            Log.d(TAG_KEY_ROTATION, "Key '$alias' does not require rotation yet.")
        }


        val calendar = Calendar.getInstance()
        calendar.time = keyValidityEndDate
        calendar.add(Calendar.DAY_OF_YEAR, CryptoConstants.ROTATION_INTERVAL_DAYS)
        val rotationDate = calendar.time

        if (currentDate.after(rotationDate)) {
            rotateKey(alias, rotationDate)
        } else {
            Log.d(TAG_KEY_ROTATION, "Key '$alias' does not require rotation yet.")
        }
    }

    fun isKeyRotationNeeded(alias: String): Boolean {
        val keyInfo = KeyHelper.getKeyInfo(alias) ?: return false
        val keyValidityEndDate = keyInfo.keyValidityForOriginationEnd ?: return false
        val currentDate = Date()
        return currentDate.after(keyValidityEndDate)
    }

    private fun rotateKey(alias: String, rotationDate: Date) {
        try {
            KeyHelper.generateAESKey(alias)
            Log.d(TAG_KEY_ROTATION, String.format(CryptoConstants.LOG_ROTATION_SUCCESS, alias))
        } catch (e: Exception) {
            Log.e(TAG_KEY_ROTATION, String.format(CryptoConstants.LOG_ROTATION_FAILED, alias, e.message))
        }
    }

}
