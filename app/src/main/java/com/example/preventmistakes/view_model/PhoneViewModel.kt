package com.example.preventmistakes.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.preventmistakes.PhoneDirEntity
import com.example.preventmistakes.PhoneRepository

class PhoneViewModel(application: Application) : ViewModel() {

    private val repository = PhoneRepository(application)

    fun getAll(): List<PhoneDirEntity> {
        return repository.getAll()
    }

    fun isBlocked(phoneNumber: String): Boolean {

        return repository.isBlocked(phoneNumber)
    }

    fun blockPhone(phone: PhoneDirEntity) {

        repository.blockPhone(phone)
    }

    fun unblockPhone(phone: PhoneDirEntity) {

        repository.unblockPhone(phone)
    }
}