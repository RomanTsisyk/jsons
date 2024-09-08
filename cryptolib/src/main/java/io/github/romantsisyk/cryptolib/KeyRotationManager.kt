package io.github.romantsisyk.cryptolib

import android.util.Log
import java.util.Calendar
import java.util.Date

object KeyRotationManager {

    private const val TAG = "KeyRotationManager"
    private const val ROTATION_INTERVAL_DAYS = 90

    /**
     * Checks if the key needs rotation based on the rotation interval.
     * If rotation is needed, generates a new key and deletes the old one.
     */
    fun rotateKeyIfNeeded(alias: String) {
        val keyInfo = KeyHelper.getCustomKeyInfo(alias) ?: return
        val keyValidityEndDate = keyInfo.keyValidityForOriginationEnd ?: return
        val currentDate = Date()

        if (currentDate.after(keyValidityEndDate)) {
            try {
                KeyHelper.generateAESKey(alias)
                Log.d(TAG, "Key '$alias' rotated successfully.")
            } catch (e: Exception) {
                Log.e(TAG, "Key rotation failed for '$alias': ${e.message}")
            }
        } else {
            Log.d(TAG, "Key '$alias' does not require rotation yet.")
        }


        val calendar = Calendar.getInstance()
        calendar.time = keyValidityEndDate
        calendar.add(Calendar.DAY_OF_YEAR, ROTATION_INTERVAL_DAYS)
        val rotationDate = calendar.time

        if (currentDate.after(rotationDate)) {
            try {
                KeyHelper.generateAESKey(alias)
                Log.d(TAG, "Key '$alias' rotated successfully.")
            } catch (e: Exception) {
                Log.e(TAG, "Key rotation failed for '$alias': ${e.message}")
            }
        } else {
            Log.d(TAG, "Key '$alias' does not require rotation yet.")
        }
    }
}
