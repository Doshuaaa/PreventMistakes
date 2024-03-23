package com.example.preventmistakes.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PhoneDirViewModelFactory(private val application: Application): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(PhoneDirViewModel::class.java)) {
            return PhoneDirViewModel(application) as T
        }
        throw IllegalArgumentException("")
    }
}