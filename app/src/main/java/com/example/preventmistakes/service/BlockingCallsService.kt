package com.example.preventmistakes.service


import android.app.Activity
import android.app.Notification
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.os.Build
import android.os.IBinder
import android.view.Window
import androidx.core.content.ContextCompat
import com.example.preventmistakes.NOTIFICATION_ID
import com.example.preventmistakes.NotificationHelper
import com.example.preventmistakes.R
import com.example.preventmistakes.activity.MainActivity
import com.example.preventmistakes.activity.currWindow

const val SERVICE_COMMAND = "service_command"


class BlockingCallsService : Service() {

    private val helper by lazy { NotificationHelper(this) }
    private lateinit var notification: Notification
    private lateinit var state: ServiceState

    private val cancelReceiver = ServiceReceiver()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        notification = helper.getNotification()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(cancelReceiver, IntentFilter("delete_notification"), RECEIVER_EXPORTED)
            registerReceiver(cancelReceiver, IntentFilter("stop_service"), RECEIVER_EXPORTED)

            val state = intent.extras?.getSerializable(SERVICE_COMMAND, ServiceState::class.java)
            this.state = state!!
            when(state) {
                ServiceState.START -> {
                    startForegroundService()
                }

                ServiceState.STOP -> {
                    stopForegroundService()
                }
            }
        } else {
            registerReceiver(cancelReceiver, IntentFilter("delete_notification"))
            registerReceiver(cancelReceiver, IntentFilter("stop_service"))

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
        unregisterReceiver(cancelReceiver)
        stopSelf()
    }

    inner class ServiceReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if(intent.action == "delete_notification") {
                if(state == ServiceState.START) {
                    helper.notifyNotification()
                }
            }
            if(intent.action == "stop_service") {
                MainActivity.viewModel.setIsServiceRunning(false)
                setStatusBar(context)
                unregisterReceiver(this)
            }
        }

        private fun setStatusBar(context: Context) {
            currWindow.statusBarColor = ContextCompat.getColor(context, R.color.white)
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}