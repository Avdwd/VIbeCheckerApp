package com.example.vibechecker.data.notification


import android.R.attr.description
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.vibechecker.MainActivity
import com.example.vibechecker.R

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "daily_reminder_channel"

        //Створюємо канал (потрібно для Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    channelId,
            "Щоденні нагадування",
            NotificationManager.IMPORTANCE_HIGH // <--- БУЛО DEFAULT

            ).apply {
                description = "Нагадування записати настрій"
            }
            notificationManager.createNotificationChannel(channel)
        }

        //переходимо при кліку (на MainActivity)
        val contentIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        //Будуємо сповіщення
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Як пройшов твій день?")
            .setContentText("Запиши свій вайб, щоб не забути! \uD83D\uDE0A")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Прибирати після кліку
            .build()

        //Показуємо
        notificationManager.notify(1001, notification)
    }
}