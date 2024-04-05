package com.example.preventmistakes.activity

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.preventmistakes.R
import com.example.preventmistakes.adapter.SearchResultAdapter
import com.example.preventmistakes.databinding.ActivityBlockingByInputNumBinding
import com.example.preventmistakes.model.PhonePosition
import com.example.preventmistakes.view_model.PhoneDirViewModel
import com.example.preventmistakes.view_model.PhoneDirViewModelFactory
import com.example.preventmistakes.view_model.SearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class BlockingByInputNumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlockingByInputNumBinding
    private val phoneDirViewModel: PhoneDirViewModel by viewModels { PhoneDirViewModelFactory(application) }
    private val searchViewModel: SearchViewModel by viewModels()
    private var resultList: List<PhonePosition> = listOf()
    private val searchAdapter: SearchResultAdapter by lazy { SearchResultAdapter(resultList, this) }

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
        binding.searchViewModel = searchViewModel
        binding.lifecycleOwner = this

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
                    searchViewModel.searchList(editTable.toString())

                    //
                } else {
                    listOf()
                }
                searchAdapter.list = resultList
                searchAdapter.notifyDataSetChanged()
            }
        })
    }

}