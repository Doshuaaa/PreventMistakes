package com.example.preventmistakes.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.preventmistakes.R
import com.example.preventmistakes.adapter.SearchResultAdapter
import com.example.preventmistakes.databinding.ActivitySearchBinding
import com.example.preventmistakes.model.Phone
import com.example.preventmistakes.model.PhonePosition
import com.example.preventmistakes.view_model.SearchViewModel

class SearchActivity : AppCompatActivity() {

    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var binding: ActivitySearchBinding
    var resultList: List<PhonePosition> = listOf()
    private val searchAdapter: SearchResultAdapter by lazy { SearchResultAdapter(resultList, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val phoneList = intent.intentSerializable("phone_dir_list", Array<Phone>::class.java)!!.toList()


        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        searchViewModel.apply {
            setPhoneList(phoneList)
        }

        binding.searchResultRecyclerView.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity)
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editTable: Editable) {
                resultList = if(editTable.toString() != "") {
                    searchViewModel.searchList(editTable.toString())
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
        if(searchAdapter.selectedPosition != -1) {
            val prefs = getSharedPreferences("changeable_data", Context.MODE_PRIVATE)
            prefs.edit().putInt("changeable_phone_index",searchAdapter.list[searchAdapter.selectedPosition].phone.index).apply()
            searchAdapter.notifyItemChanged(searchAdapter.selectedPosition)
        }
    }

    private fun Intent.intentSerializable(key: String, clazz: Class<Array<Phone>>) : Array<Phone>? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.getSerializableExtra(key, clazz)
        } else {
            this.getSerializableExtra(key) as Array<Phone>?
        }
    }
}