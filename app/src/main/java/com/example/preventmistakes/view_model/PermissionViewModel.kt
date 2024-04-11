package com.example.preventmistakes.view_model

import android.content.pm.PackageManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PermissionViewModel : ViewModel() {

    private var _permissionMap = MutableLiveData<HashMap<String, Int>>()
    val permissionMap get() = _permissionMap

    private var _visibleFlag = MutableLiveData<Boolean>()

    val visibleFlag get() = _visibleFlag

    var firstRequestFlag = true

    fun setVisibleFlag(){
        for(permission in _permissionMap.value?.toList()!!) {
            if(permission.second == PackageManager.PERMISSION_DENIED) {
                _visibleFlag.value = false
                return
            }
        }
        _visibleFlag.value = true
    }
}