package com.example.preventmistakes.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.preventmistakes.PhoneDirEntity
import com.example.preventmistakes.PhoneRepository
import com.example.preventmistakes.adapter.BlockedPhoneAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class BlockedPhoneViewModel(application: Application) : ViewModel() {

    private val repository = PhoneRepository(application)
    private lateinit var _blockedPhoneList: MutableList<PhoneDirEntity>

    init {
        setBlockedPhoneList()
    }

    val blockedPhoneList get() = _blockedPhoneList

    private fun setBlockedPhoneList() {
        _blockedPhoneList = repository.getAll().toMutableList()
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

    fun updateBlockedPhone(adapter: BlockedPhoneAdapter, selectedItem: Int) {

        if(!isBlocked(blockedPhoneList[adapter.selectedItem].phoneNumber)) {
            blockedPhoneList.removeAt(selectedItem)
            adapter.notifyDataSetChanged()    // notifyItemRemoved()를 안 쓴 이유는 각 viewholder의 position을 업데이트 해주어야 하기 떄문(Out of Bounds 발생))
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