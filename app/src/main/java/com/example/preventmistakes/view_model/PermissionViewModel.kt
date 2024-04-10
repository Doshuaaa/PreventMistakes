package com.example.preventmistakes.view_model

import android.Manifest
import android.content.pm.PackageManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PermissionViewModel : ViewModel() {

    private val _permissionMap =  MutableLiveData<HashMap<String, Int>>()
    val permissionMap get() = _permissionMap.value!!

    init {
        _permissionMap.value = hashMapOf()
    }
    fun setPermissionMap(map: HashMap<String, Int>) {
        _permissionMap.value = map
    }
}