package com.example.dzialajproszelodowka.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
//import com.example.dzialajproszelodowka.R
import com.example.dzialajproszelodowka.data.db.AppDatabase
import com.example.dzialajproszelodowka.data.repository.ProductRepository

class ExpiryWorker(
    context: Context,
    params: WorkerParameters
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = ProductRepository(database.productDao())

        val expiringProducts = repository.getProductsExpiringTomorrow()

        if (expiringProducts.isNotEmpty()) {
            val productNames = expiringProducts.joinToString(", ") { it.name }
            showNotification("Uwaga! Produkty tracą ważność", "Jutro wygasa: $productNames")
        }

        return Result.success()
    }

    private fun showNotification(title: String, content: String) {
        val context = applicationContext
        val channelId = "expiry_channel"
        val notificationId = 1

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Powiadomienia o produktach",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        try {
            notificationManager.notify(notificationId, notification)
        } catch (e: SecurityException) {

        }
    }
}