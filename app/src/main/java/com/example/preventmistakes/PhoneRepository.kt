package com.example.preventmistakes

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class PhoneRepository(application: Application) {

    private val phoneDataBase = PhoneDataBase.getInstance(application)!!
    private val phoneDao = phoneDataBase.phoneDao()

    fun getAll(): List<PhoneDirEntity> {

        var phones = listOf<PhoneDirEntity>()
        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                phones = phoneDao.getAll()
            }.join()
        }
        return phones
    }


    fun isBlocked(phoneNumber: String) : Boolean{

        var isBlocked = false
        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                isBlocked = when(phoneDao.isPhoneBlocked(phoneNumber)) {

                    0 -> false
                    else -> true
                }
            }.join()
        }

        return isBlocked
    }

    fun blockPhone(phone: PhoneDirEntity) {

        try {
            val thread = Thread(Runnable {
                phoneDao.blockPhone(phone)
            })
            thread.start()
        } catch (_: Exception) { }
    }

    fun unblockPhone(phone: PhoneDirEntity) {

        try {
            val thread = Thread(Runnable {
                phoneDao.unblockPhone(phone)
            })
            thread.start()
        } catch (_: Exception) { }
    }
}