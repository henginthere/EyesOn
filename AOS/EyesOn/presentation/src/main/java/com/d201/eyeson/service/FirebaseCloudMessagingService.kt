package com.d201.eyeson.service

import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.d201.domain.model.Noti
import com.d201.domain.repository.NotiRepository
import com.d201.eyeson.R
import com.d201.eyeson.view.angel.help.AngelHelpActivity
import com.d201.eyeson.view.blind.notification.BlindNotiFragment
import com.d201.eyeson.view.login.LoginActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

private const val TAG = "FirebaseCloudMessagingService"

@AndroidEntryPoint
class FirebaseCloudMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notiRepository: NotiRepository

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: ${token}")
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.notification.let {
            val messageTitle = it!!.title
            val messageContent = it!!.body

            notiRepository.insertNoti(Noti(0, messageTitle!!, messageContent!!))

            LocalBroadcastManager.getInstance(this).sendBroadcast(Intent("ReceiveNoti"))

            val mainIntent = Intent(this, AngelHelpActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val mainPendingIntent = PendingIntent.getActivity(
                this, 0, mainIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
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