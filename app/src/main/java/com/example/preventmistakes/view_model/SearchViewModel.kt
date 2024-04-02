package com.example.preventmistakes.view_model

import androidx.lifecycle.ViewModel
import com.example.preventmistakes.model.Phone
import com.example.preventmistakes.model.PhonePosition
import java.lang.NumberFormatException

class SearchViewModel : ViewModel() {


    private lateinit var _phoneList: List<Phone>


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

}