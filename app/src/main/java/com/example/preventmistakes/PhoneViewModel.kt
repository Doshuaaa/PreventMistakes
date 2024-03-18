package com.example.preventmistakes

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhoneViewModel(application: Application) : ViewModel() {

    private val repository = PhoneRepository(application)
    private val phones = repository.getAll()



    fun getAll(): LiveData<List<PhoneDirEntity>> {

        return phones
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