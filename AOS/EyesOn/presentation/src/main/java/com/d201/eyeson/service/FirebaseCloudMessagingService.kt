package com.d201.eyeson.service

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.d201.eyeson.R
import com.d201.eyeson.view.angel.help.AngelHelpActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private const val TAG = "FirebaseCloudMessagingService"

class FirebaseCloudMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: ${token}")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.notification.let {
            val messageTitle = it!!.title
            val messageContent = it!!.body

//            NoticeMessageUtil.setMessageToSharedPreference(messageContent.toString())

            val mainIntent = Intent(this, AngelHelpActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val mainPendingIntent = PendingIntent.getActivity(this, 0, mainIntent, 0)
            val builder = NotificationCompat.Builder(this, "EyesOn_id")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(messageTitle)
                .setContentText(messageContent)
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent)

            NotificationManagerCompat.from(this).apply {
                notify(201, builder.build())
            }

        }
    }
}