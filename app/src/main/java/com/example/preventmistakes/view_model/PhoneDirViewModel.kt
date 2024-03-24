package com.example.preventmistakes.view_model

import android.app.Application
import android.content.Context
import android.provider.ContactsContract
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.preventmistakes.PhoneRepository
import com.example.preventmistakes.model.Phone
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PhoneDirViewModel(application: Application) : ViewModel() {

    private val _phoneList = MutableLiveData<List<Phone>>()
    val phoneList get() = _phoneList
    val repository = PhoneRepository(application)

    fun setPhoneList(context: Context) {
        val resolver = context.contentResolver!!
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val cursor = resolver.query(phoneUri, projection, null, null, null)
        val numberList = arrayListOf<Phone>()

        if(cursor != null) {
            while(cursor.moveToNext()) {
                val nameIndex = cursor.getColumnIndex(projection[1])
                val numberIndex = cursor.getColumnIndex(projection[2])
                val name = cursor.getString(nameIndex)
                val number = cursor.getString(numberIndex).replace("-","")
                var isBlocked = true

                runBlocking {
                    val job =  CoroutineScope(Dispatchers.Default).launch {
                        isBlocked = repository.isBlocked(number)
                    }
                    //job.start()
                    job.join()
                    //job.cancel()
                }
                numberList.add(Phone(name, number, isBlocked))
            }
        }
        cursor?.close()
        numberList.sortBy { it.name }
        _phoneList.postValue(numberList)
    }
}