package com.example.preventmistakes

import android.app.Notification
import android.app.Notification.FOREGROUND_SERVICE_IMMEDIATE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.preventmistakes.activity.MainActivity
import com.example.preventmistakes.activity.Receiver
import com.example.preventmistakes.service.BlockingCallsService
import com.example.preventmistakes.service.SERVICE_COMMAND
import com.example.preventmistakes.service.ServiceState

const val NOTIFICATION_ID = 100


class NotificationHelper(private val context: Context) {

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
   // private val stopIntent = Intent(context, BlockingCallsService.StopServiceReceiver::class.java)

    lateinit var stopPendingIntent: PendingIntent
    init {
        //context.registerReceiver(Receiver(), IntentFilter("s"), AppCompatActivity.RECEIVER_NOT_EXPORTED)
        val intent = Intent(context, Receiver::class.java)
        stopPendingIntent = PendingIntent.getBroadcast(context, 10, intent , PendingIntent.FLAG_IMMUTABLE)
    }

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val builder: NotificationCompat.Builder by lazy {

       // stopIntent.action = "stop_service"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            //context.registerReceiver(MainActivity().Receiver(), IntentFilter("stop_service"), Service.RECEIVER_EXPORTED)

            NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("PreventMistakes")
                .setContentText("발신 차단 실행중")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setForegroundServiceBehavior(FOREGROUND_SERVICE_IMMEDIATE)
                .setDeleteIntent( PendingIntent.getBroadcast(context, 0, Intent("delete_notification"),
                    PendingIntent.FLAG_IMMUTABLE))
                .addAction(NotificationCompat.Action.Builder(android.R.drawable.sym_def_app_icon, "발신 차단 비활성화", stopPendingIntent).build())
        } else {
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("PreventMistakes")
                .setContentText("발신 차단 실행중")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
        }
    }

    private fun createChannel() {

        //https://developer.android.com/about/versions/14/behavior-changes-all?hl=ko#non-dismissable-notifications 에 의하면 ongoing 동작 안함.
        notificationManager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_NONE
            )
        )
    }

    fun getNotification(): Notification {
        createChannel()
        return builder.build()
    }
    fun notifyNotification() {
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    inner class BroadText : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent) {
            if(intent.action == "stop_service") {
                val prefs = context.getSharedPreferences("stop_action_flag", Context.MODE_PRIVATE)
                prefs.edit().putBoolean("flag", true).apply()
                val intent = Intent(context, BlockingCallsService::class.java)

                intent.putExtra(SERVICE_COMMAND, ServiceState.STOP)
                ContextCompat.startForegroundService(context, intent)
            }
        }
    }
}