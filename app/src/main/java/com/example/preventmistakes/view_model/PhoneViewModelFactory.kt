package com.example.preventmistakes.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PhoneViewModelFactory(private val application: Application): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PhoneViewModel::class.java)) {
            return PhoneViewModel(application) as T
        }
        throw IllegalArgumentException("")
    }
}