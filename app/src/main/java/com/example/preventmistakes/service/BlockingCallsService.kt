package com.example.preventmistakes.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.example.preventmistakes.NOTIFICATION_ID
import com.example.preventmistakes.NotificationHelper


class BlockingCallsService : Service() {

    @RequiresApi(34)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val helper = NotificationHelper(this)
        startForeground(NOTIFICATION_ID, helper.getNotification(), FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        helper.notifyNotification()

        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

}