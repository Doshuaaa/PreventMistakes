package com.example.preventmistakes.view_model

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private val _permissionVisibleList = MutableLiveData<Array<Int>>()
    private val _phonePermissionVisibility = MutableLiveData<Int>()
    private val _readPermissionVisibility = MutableLiveData<Int>()

    init {
        _phonePermissionVisibility.value = View.GONE
        _readPermissionVisibility.value = View.GONE
    }

    val permissionVisibleList get() = _permissionVisibleList
    val phonePermissionVisibility get() = _phonePermissionVisibility
    val readPermissionVisibility get() = _readPermissionVisibility


    fun getTextVisibility(permissionName: String) : Int {
        when(permissionName) {
            "phone" -> return _phonePermissionVisibility.value!!
            "read" -> return _readPermissionVisibility.value!!
        }
        return View.GONE
    }


    fun setPermissionVisibility(permissionName: String, visibility: Int) {

        when(permissionName) {
            "phone" -> _phonePermissionVisibility.value = visibility
            "read" -> _readPermissionVisibility.value = visibility
        }
    }
}