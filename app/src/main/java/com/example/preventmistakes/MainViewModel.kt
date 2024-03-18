package com.example.preventmistakes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private val _permissionVisibleList = MutableLiveData<Array<Int>>()

    val permissionVisibleList get() = _permissionVisibleList



    fun getTextVisibility(index: Int) : Int {
        return _permissionVisibleList.value?.get(index) ?: 8
    }


    fun setPermissionVisibleList(array: Array<Int>) {

        _permissionVisibleList.value = array
    }
}