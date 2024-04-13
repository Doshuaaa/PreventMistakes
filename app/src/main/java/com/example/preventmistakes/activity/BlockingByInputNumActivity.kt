package com.example.preventmistakes.activity

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.preventmistakes.PhoneDirEntity
import com.example.preventmistakes.R
import com.example.preventmistakes.adapter.SearchResultAdapter
import com.example.preventmistakes.databinding.ActivityBlockingByInputNumBinding
import com.example.preventmistakes.model.PhonePosition
import com.example.preventmistakes.view_model.PhoneDirViewModel
import com.example.preventmistakes.view_model.PhoneDirViewModelFactory
import com.example.preventmistakes.view_model.PhoneViewModel
import com.example.preventmistakes.view_model.PhoneViewModelFactory
import com.example.preventmistakes.view_model.SearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class BlockingByInputNumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlockingByInputNumBinding
    private val phoneDirViewModel: PhoneDirViewModel by viewModels { PhoneDirViewModelFactory(application) }
    private val searchViewModel: SearchViewModel by viewModels()
    private val phoneViewModel: PhoneViewModel by viewModels { PhoneViewModelFactory(application) }
    private var resultList: List<PhonePosition> = listOf()
    private val searchAdapter: SearchResultAdapter by lazy { SearchResultAdapter(resultList, this) }
    private val prefs: SharedPreferences by lazy { getSharedPreferences("changeable_data", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchViewModel.setPhoneList(
            runBlocking {
                CoroutineScope(Dispatchers.IO).async {
                    phoneDirViewModel.setPhoneList(this@BlockingByInputNumActivity)
                }.await()
            }
        )
        binding = DataBindingUtil.setContentView(this, R.layout.activity_blocking_by_input_num)
        currWindow = window
        with(binding) {
            searchViewModel = this@BlockingByInputNumActivity.searchViewModel
            lifecycleOwner = this@BlockingByInputNumActivity
            activity = this@BlockingByInputNumActivity
        }

        with(binding.currNumEditText) {
            //setSelection(length())
            showSoftInputOnFocus = false
        }

        with(binding.searchByNumberRecyclerView) {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(this@BlockingByInputNumActivity)
        }

        binding.currNumEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editTable: Editable) {
                resultList = if(editTable.toString() != "") {
                    searchViewModel.searchList(editTable.toString().replace("-", ""))
                } else {
                    listOf()
                }
                searchAdapter.list = resultList
                searchAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun onResume() {
        super.onResume()


        val index = prefs.getInt("changeable_phone_index", -1)

        if(index != -1) {
            modifyPhoneListBlocked(index)
            searchAdapter.notifyItemChanged(index)
            prefs.edit().putInt("changeable_phone_index", -1).apply()
        }
    }

    private fun modifyPhoneListBlocked(index: Int) {
        try {
            searchAdapter.list[index].phone.blocked = phoneViewModel.isBlocked(searchAdapter.list[index].phone.number)
        } catch (_: IndexOutOfBoundsException) { }
    }

    fun blockPhone() {
        val num = searchViewModel.currNum.value
        if(num != null) {

            if(!phoneViewModel.isBlocked(num)) {
                val dlg = AlertDialog.Builder(this)
                with(dlg) {
                    setMessage("${searchViewModel.currNumFormatted.value} 번호를 발신 차단할까요?")
                    setPositiveButton("확인"
                    ) { _, _ ->
                        val corrToNumber = searchViewModel.getNameCorrespondingToNumber(num)
                        phoneViewModel.blockPhone(PhoneDirEntity(num, corrToNumber))
                        if(corrToNumber != "") {
                            searchViewModel.setPhoneList(
                                runBlocking {
                                    CoroutineScope(Dispatchers.IO).async {
                                        phoneDirViewModel.setPhoneList(this@BlockingByInputNumActivity)
                                    }.await()
                                }
                            )
                        }
                        searchViewModel.clearCurrNum()
                    }
                    setNegativeButton("취소"
                    ) { _, _ -> }
                    show()
                }
            } else {
                val dlg = AlertDialog.Builder(this)
                with(dlg) {
                    setMessage("${searchViewModel.currNumFormatted.value}는 이미 차단된 번호입니다.")
                    setPositiveButton("확인"
                    ) { _, _ -> }
                    show()
                }
            }
        }
    }
}