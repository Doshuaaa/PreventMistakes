package com.example.preventmistakes.service


import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.example.preventmistakes.NOTIFICATION_ID
import com.example.preventmistakes.NotificationHelper
import java.io.Serializable

const val SERVICE_COMMAND = "service_command"


class BlockingCallsService : Service() {

    private val helper by lazy { NotificationHelper(this) }
    private lateinit var notification: Notification
    private lateinit var state: ServiceState

    private val receiver = NotificationCancelReceiver()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        notification = helper.getNotification()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, IntentFilter("delete_notification"), RECEIVER_EXPORTED)
        } else {
            registerReceiver(receiver, IntentFilter("delete_notification"))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val state = intent.extras?.getSerializable(SERVICE_COMMAND, ServiceState::class.java)
            this.state = state!!
            when(state) {
                ServiceState.START -> { startForegroundService() }
                ServiceState.STOP -> { stopForegroundService() }
                else -> { stopForegroundService() }
            }
        } else {
            val state = intent.extras?.getSerializable(SERVICE_COMMAND) as ServiceState
            this.state = state
            when(state) {
                ServiceState.START -> { startForegroundService() }
                ServiceState.STOP -> { stopForegroundService() }
            }
        }

        return START_STICKY
    }


    private fun startForegroundService() {

        if (Build.VERSION.SDK_INT >= 34) {
            startForeground(NOTIFICATION_ID, notification, FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
        helper.notifyNotification()
    }

    private fun stopForegroundService() {
        unregisterReceiver(receiver)
        stopSelf()
    }


    inner class NotificationCancelReceiver: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if(state == ServiceState.START) {
                helper.notifyNotification()
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}