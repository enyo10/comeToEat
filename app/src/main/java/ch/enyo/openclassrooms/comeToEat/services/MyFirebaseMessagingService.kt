package ch.enyo.openclassrooms.comeToEat.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import ch.enyo.openclassrooms.comeToEat.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {


    companion object{
        private const val TAG: String= "FirebaseMessagingService"
    }

    private val NOTIFICATIONID = 7
    private val NOTIFICATIONTAG = "FIREBASEOC"

   /* override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("$TAG token --> $token")
    }*/

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        try {

            if (remoteMessage.notification != null) {
                // 1 - Get message sent by Firebase
                val message = remoteMessage.notification!!.body
                //2 - Show message in console
                Log.d("TAG", "message :-- $message")

                Log.d(TAG, "message -- $remoteMessage")
               showNotification(
                    remoteMessage.notification?.title,
                    remoteMessage.notification?.body
                )
            } else {
                showNotification(remoteMessage.data["title"], remoteMessage.data["message"])
                Log.d(TAG, " error")
            }

        } catch (e: Exception) {
            println("$TAG error -->${e.localizedMessage}")
        }
    }

    private fun showNotification(
        title: String?,
        body: String?
    ) {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.channel_id)
        val channelName = getString(R.string.channel_name)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupNotificationChannels(channelId, channelName, notificationManager)
        }

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

       notificationManager.notify(0, notificationBuilder.build())
        // 7 - Show notification
       // notificationManager.notify(NOTIFICATIONTAG, NOTIFICATIONID, notificationBuilder.build())
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupNotificationChannels(
        channelId: String,
        channelName: String,
        notificationManager: NotificationManager
    ) {

        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
        channel.enableLights(true)
        channel.lightColor = Color.GREEN
        channel.enableVibration(true)
        notificationManager.createNotificationChannel(channel)
    }
}