package com.app.fundamentalsubmission.di

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.app.fundamentalsubmission.R

class DailyReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        // Ambil event terdekat
        val eventName = inputData.getString("eventName")
        val eventDate = inputData.getString("eventDate")

        if (eventName != null && eventDate != null){
            showNotification(applicationContext, eventName, eventDate)
        }

        return Result.success()
    }

    private fun showNotification(context: Context, eventName: String, eventDate: String) {
        val channel = NotificationChannel(
            "channel_id",
            "DailyReminder",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifikasi $eventName in date $eventDate"
        }

        // Buat NotificationChannel
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, "channel_id")
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("Event Terdekat")
            .setContentText("$eventName pada $eventDate")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("Notification", "Permission not granted for notifications.")
            return
        }
        notificationManager.notify(1, notification)
    }
}
