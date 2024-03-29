package com.example.preventmistakes.view_model

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

    fun setIsServiceRunning(isChecked: Boolean) {

        _isServiceRunning.value = isChecked
    }
}