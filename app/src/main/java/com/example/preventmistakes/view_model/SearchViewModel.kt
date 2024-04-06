package com.example.preventmistakes.view_model

import android.telephony.PhoneNumberUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.preventmistakes.model.Phone
import com.example.preventmistakes.model.PhonePosition
import java.lang.NumberFormatException
import java.util.Locale

class SearchViewModel : ViewModel() {


    private lateinit var _phoneList: List<Phone>

    private var _currNum: MutableLiveData<String> = MutableLiveData()
    private var _currNumFormatted: MutableLiveData<String> = MutableLiveData()
    val currNum get() = _currNum
    val currNumFormatted get() = _currNumFormatted


    init {
        _currNum.value = ""
    }
    fun setPhoneList(list: List<Phone>) {
        _phoneList = list
    }

    fun searchList(text: String): List<PhonePosition> {

        val list = mutableListOf<PhonePosition>()
        try {
            text.toInt()
            for(phone in _phoneList) {
                if(phone.number.contains(text)) {
                    list.add(PhonePosition(phone, phone.name.indexOf(text)))
                }
            }

        } catch (e: NumberFormatException) {
            for(phone in _phoneList) {
                if(phone.name.contains(text)) {

                    list.add(PhonePosition(phone, phone.name.indexOf(text)))
                }
                else if(phone.name.contains(text.uppercase())) {
                    list.add(PhonePosition(phone, phone.name.indexOf(text)))
                }
            }
        }

        list.sortBy { it.index }
        return list
    }

    fun updateCurrNum(num: String) {

        _currNum.value = _currNum.value + num
        _currNumFormatted.value = PhoneNumberUtils.formatNumber(_currNum.value, Locale.getDefault().country)
    }

    fun backspace() {

        _currNum.value = _currNum.value!!.replaceFirst(".$".toRegex(), "")
    }

}