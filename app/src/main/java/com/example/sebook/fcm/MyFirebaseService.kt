package com.example.sebook.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.sebook.MainActivity
import com.example.sebook.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        // Ambil title/body dari notification payload kalau ada
        var title = message.notification?.title
        var body = message.notification?.body

        // Kalau kirimnya pakai data payload, ambil dari message.data
        if (title == null) {
            title = message.data["title"] ?: "Notifikasi"
        }
        if (body == null) {
            body = message.data["body"] ?: ""
        }
        Log.d("MyFirebaseService", "showNotification with title='$title', body='$body'")

        showNotification(title, body)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Di sini biasanya token dikirim ke server agar bisa kirim FCM ke device ini
        Log.d("MyFirebaseService", "FCM token: $token")
    }

    private fun showNotification(title: String, body: String) {
        val channelId = "review_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Review Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

        // PendingIntent supaya ketika notifikasi di-tap akan membuka MainActivity
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            else PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notif = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        try {
            NotificationManagerCompat.from(this).notify(1001, notif)
        } catch (e: SecurityException) {
            // Android 13+ user menolak izin notifikasi
            Log.w("MyFirebaseService", "Notification permission denied", e)
        }
    }

}