package com.example.preventmistakes

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.preventmistakes.activity.MainActivity

const val NOTIFICATION_ID = 100

class NotificationHelper(context: Context) {

    companion object {
        const val CHANNEL_ID = "PreventMistakes00"
        const val CHANNEL_NAME = "PreventMistakes"
    }


    private val pushIntent = Intent(context, MainActivity::class.java)
    private val pendingIntent: PendingIntent = PendingIntent.getActivity(
        context,
        0,
        pushIntent,
        PendingIntent.FLAG_IMMUTABLE,

    )


    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val builder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("PreventMistakes")
            .setContentText("발신 차단 실행중")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
    }

    private fun createChannel() {

        //https://developer.android.com/about/versions/14/behavior-changes-all?hl=ko#non-dismissable-notifications 에 의하면 ongoing 동작 안함.
        notificationManager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )
    }

    fun getNotification(): Notification {
        createChannel()
        val notification = builder.build()
        notification.flags = Notification.FLAG_NO_CLEAR
        return notification
    }
    fun notifyNotification() {
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }
}