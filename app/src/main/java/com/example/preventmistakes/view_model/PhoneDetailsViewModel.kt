package com.example.preventmistakes.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhoneDetailsViewModel : ViewModel() {

    val isBlocked =  MutableLiveData<Boolean>()


    fun setBlocked(isBlocked: Boolean) {
        this.isBlocked.value = isBlocked
    }
}