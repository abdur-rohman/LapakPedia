package dev.rohman.lapakpedia.utils.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.rohman.lapakpedia.R
import dev.rohman.lapakpedia.utils.preferences.LocalStorage
import dev.rohman.lapakpedia.utils.ConstantUtil
import org.koin.android.ext.android.inject

class MessagingService : FirebaseMessagingService() {

    private val localStorage by inject<LocalStorage>()

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        localStorage.user = localStorage.user.copy(notificationToken = token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        showNotification(remoteMessage.data)
    }

    private fun showNotification(data: MutableMap<String, String>) {
        val title = data["title"] ?: ""
        val body = data["body"] ?: ""
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ConstantUtil.NOTIFICATION_CHANNEL_ID,
                ConstantUtil.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationManager?.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, ConstantUtil.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setColor(getColor(R.color.teal_700))
            .setAutoCancel(true)
            .build()

        notificationManager?.notify(ConstantUtil.NOTIFICATION_ID, notification)
    }
}