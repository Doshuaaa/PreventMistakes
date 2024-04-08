package com.example.preventmistakes.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.preventmistakes.PhoneDirEntity
import com.example.preventmistakes.PhoneRepository
import com.example.preventmistakes.adapter.BlockedPhoneByDirAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class BlockedPhoneViewModel(application: Application) : ViewModel() {

    private val repository = PhoneRepository(application)
    private val _blockedPhoneByDirList: MutableList<PhoneDirEntity> = mutableListOf()
    private val _blockedPhoneByNumberList: MutableList<PhoneDirEntity> = mutableListOf()

    init {
        setBlockedPhoneList()
    }

    val blockedPhoneByDirList get() = _blockedPhoneByDirList
    val blockedPhoneByNumberList get() = _blockedPhoneByNumberList

    private fun setBlockedPhoneList() {
        val phoneList = repository.getAll()


        for(phone in phoneList) {
            if(phone.name != "") {
                _blockedPhoneByDirList.add(phone)
            } else {
                _blockedPhoneByNumberList.add(phone)
            }
        }
    }

    private fun isBlocked(phoneNumber: String): Boolean {
        var isBlocked = false
        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                isBlocked = repository.isBlocked(phoneNumber)
            }.join()
        }
        return isBlocked
    }

    fun updateBlockedPhone(adapter: BlockedPhoneByDirAdapter, selectedItem: Int) {

        if(!isBlocked(blockedPhoneByDirList[adapter.selectedItem].phoneNumber)) {
            blockedPhoneByDirList.removeAt(selectedItem)
            //adapter.blockedList.removeAt(selectedItem)
            adapter.notifyDataSetChanged() // notifyItemRemoved()를 안 쓴 이유는 각 viewholder의 position을 업데이트 해주어야 하기 떄문(Out of Bounds 발생))

        }
    }

    fun unBlockPhone(phone: PhoneDirEntity) {
        repository.unblockPhone(phone)
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