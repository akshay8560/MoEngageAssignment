package com.example.moengageassignment

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val notificationId = 101
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (remoteMessage.data.isNotEmpty()) {

            sendNotification(remoteMessage.data["message"])
        }


        if (remoteMessage.notification != null) {
            sendNotification(remoteMessage.notification?.body)
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(messageBody: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(androidx.core.R.drawable.notification_bg)
            .setContentTitle("My notification")
            .setContentText(messageBody)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }



    }


}
