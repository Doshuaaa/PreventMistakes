package com.example.preventmistakes.view_model

import android.app.Application
import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.preventmistakes.PhoneRepository
import com.example.preventmistakes.adapter.PhoneDirAdapter
import com.example.preventmistakes.model.Phone

class PhoneDirViewModel(application: Application) : ViewModel() {

    private val _phoneList = MutableLiveData<List<Phone>>()
    lateinit var checkList: MutableList<Boolean>

    val phoneList get() = _phoneList
    private val repository = PhoneRepository(application)

    fun setPhoneList(context: Context): ArrayList<Phone> {
        val resolver = context.contentResolver!!
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val numberList = arrayListOf<Phone>()
        val cursor = resolver.query(phoneUri, projection, null, null, null)

        if(cursor != null) {
            while(cursor.moveToNext()) {
                val nameIndex = cursor.getColumnIndex(projection[1])
                val numberIndex = cursor.getColumnIndex(projection[2])
                val name = cursor.getString(nameIndex)
                val number = cursor.getString(numberIndex).replace("-","")
                val isBlocked = repository.isBlocked(number)
                numberList.add(Phone(name, number, isBlocked, 0))
            }
        }
        cursor?.close()
        numberList.sortBy {
            it.name
        }

        for((i, phone) in numberList.withIndex()) {
            phone.index = i
        }
        return numberList
    }

    fun confirmPhoneList(numberList: ArrayList<Phone>) {

        _phoneList.value = numberList
        checkList = MutableList((_phoneList.value as ArrayList<Phone>).size) { false }

    }

    fun updatePhone(number: String): Boolean {
        return repository.isBlocked(number)
    }
}