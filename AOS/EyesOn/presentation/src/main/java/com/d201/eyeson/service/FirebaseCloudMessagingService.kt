package com.d201.eyeson.service

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.d201.domain.model.Noti
import com.d201.domain.repository.NotiRepository
import com.d201.eyeson.R
import com.d201.eyeson.view.angel.main.AngelMainActivity
import com.d201.eyeson.view.blind.BlindMainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
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
            val action = message.data["action"]

            Log.d(
                TAG,
                "onMessageReceived: title : $messageTitle\nbody : $messageContent\naction : ${action}"
            )

            val builder = when (action) {
                "AngelHelp" -> {
                    val mainIntent = Intent(this, AngelMainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        putExtra("action", action)
                    }

                    val mainPendingIntent = PendingIntent.getActivity(
                        this, 0, mainIntent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )

                    NotificationCompat.Builder(this, "EyesOn_id")
                        .setSmallIcon(R.mipmap.ic_app_logo_round)
                        .setContentTitle(messageTitle)
                        .setContentText(messageContent)
                        .setAutoCancel(true)
                        .setContentIntent(mainPendingIntent)
                }
                else -> {
                    val now = Date(System.currentTimeMillis())
                    val formatter = SimpleDateFormat("MM/dd hh:mm", Locale.KOREA)

                    val notiTime = formatter.format(now)

                    notiRepository.insertNoti(Noti(0, messageTitle!!, messageContent!!, notiTime))

                    val mainIntent = Intent(this, BlindMainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                        putExtra("action", action)
                    }

                    val mainPendingIntent = PendingIntent.getActivity(
                        this, 0, mainIntent,
                        PendingIntent.FLAG_IMMUTABLE
                    )

                    NotificationCompat.Builder(this, "EyesOn_id")
                        .setSmallIcon(R.mipmap.ic_app_logo_round)
                        .setContentTitle(messageTitle)
                        .setContentText(messageContent)
                        .setAutoCancel(true)
                        .setContentIntent(mainPendingIntent)
                }
            }

            NotificationManagerCompat.from(this).apply {
                notify(201, builder.build())
            }

        }
    }
}