package com.example.preventmistakes.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.preventmistakes.PhoneDirEntity
import com.example.preventmistakes.PhoneRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class BlockedPhoneViewModel(application: Application) : ViewModel() {

    private val repository = PhoneRepository(application)
    private lateinit var _blockedPhoneList: List<PhoneDirEntity>

    init {
        setBlockedPhoneList()
    }

    val blockedPhoneList get() = _blockedPhoneList

    private fun setBlockedPhoneList() {
        runBlocking {
            launch {
                _blockedPhoneList = repository.getAll()
            }.join()
        }
    }
}

class BlockedPhoneViewModelFactory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BlockedPhoneViewModel::class.java)) {
            return BlockedPhoneViewModel(application) as T
        }
        throw IllegalArgumentException("")

    }
}