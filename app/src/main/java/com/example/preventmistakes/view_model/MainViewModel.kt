package com.example.preventmistakes.view_model

import android.app.ActivityManager
import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.preventmistakes.service.BlockingCallsService

class MainViewModel: ViewModel() {


    private val _isServiceRunning = MutableLiveData<Boolean>()

    val isServiceRunning get() = _isServiceRunning

    fun isServiceRunning(context: Context) : Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningService = activityManager.getRunningServices(Int.MAX_VALUE)
        return runningService.any {
            it.service.className == BlockingCallsService::class.java.name
        }
    }

    fun setIsServiceRunning(isChecked: Boolean) {

        _isServiceRunning.value = isChecked
    }
}