package io.github.romantsisyk.cryptolib.crypto.keymanagement

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class KeyRotationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val keys = KeyHelper.listKeys()
        keys.forEach { alias ->
            KeyRotationManager.rotateKeyIfNeeded(alias)
        }
        return Result.success()
    }
}

object KeyRotationScheduler {

    fun scheduleKeyRotation(context: Context) {
        val rotationWork = PeriodicWorkRequestBuilder<KeyRotationWorker>(
            7, TimeUnit.DAYS // Check every week
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "KeyRotationWork",
            ExistingPeriodicWorkPolicy.KEEP,
            rotationWork
        )
    }
}
