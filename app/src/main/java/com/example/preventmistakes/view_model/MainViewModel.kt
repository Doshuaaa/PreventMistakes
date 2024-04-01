package com.example.preventmistakes.view_model

import android.app.ActivityManager
import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.preventmistakes.service.BlockingCallsService

class MainViewModel: ViewModel() {

    private val _phonePermissionVisibility = MutableLiveData<Int>()
    private val _readPermissionVisibility = MutableLiveData<Int>()

    private val _isServiceRunning = MutableLiveData<Boolean>()

    init {
        _phonePermissionVisibility.value = View.GONE
        _readPermissionVisibility.value = View.GONE
    }

    val phonePermissionVisibility get() = _phonePermissionVisibility
    val readPermissionVisibility get() = _readPermissionVisibility
    val isServiceRunning get() = _isServiceRunning

    fun setPermissionVisibility(permissionName: String, visibility: Int) {

        when(permissionName) {
            "phone" -> _phonePermissionVisibility.value = visibility
            "read" -> _readPermissionVisibility.value = visibility
        }
    }

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