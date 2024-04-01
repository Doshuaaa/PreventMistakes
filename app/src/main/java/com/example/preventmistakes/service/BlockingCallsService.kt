package com.example.preventmistakes.service


import android.app.Service
import android.content.Intent
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


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when(intent.extras?.getSerializable(SERVICE_COMMAND, ServiceState::class.java)) {
                ServiceState.START -> { startForegroundService() }
                ServiceState.STOP -> { stopForegroundService() }
                else -> { stopForegroundService() }
            }
        } else {
            when(intent.extras?.getSerializable(SERVICE_COMMAND) as ServiceState) {
                ServiceState.START -> { startForegroundService() }
                ServiceState.STOP -> { stopForegroundService() }
            }
        }

        return START_STICKY
    }


    private fun startForegroundService() {
        val helper = NotificationHelper(this)


        if (Build.VERSION.SDK_INT >= 34) {
            startForeground(NOTIFICATION_ID, helper.getNotification(), FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(NOTIFICATION_ID, helper.getNotification())
        }
        helper.notifyNotification()
    }

    private fun stopForegroundService() {
        stopSelf()
    }


    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}