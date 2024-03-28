package com.example.preventmistakes.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.preventmistakes.R
import com.example.preventmistakes.adapter.PhoneDirAdapter
import com.example.preventmistakes.databinding.ActivityPhoneDirBinding
import com.example.preventmistakes.model.Phone
import com.example.preventmistakes.view_model.PhoneDirViewModel
import com.example.preventmistakes.view_model.PhoneDirViewModelFactory
import com.example.preventmistakes.view_model.PhoneViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PhoneDirActivity : AppCompatActivity() {


    private lateinit var binding: ActivityPhoneDirBinding
    private val phoneDirViewModel: PhoneDirViewModel by viewModels{ PhoneDirViewModelFactory(application)}
    private lateinit var phoneDirAdapter: PhoneDirAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val phoneViewModel = PhoneViewModel(this.application)

        runBlocking {
            var numberList = arrayListOf<Phone>()
            CoroutineScope(Dispatchers.IO).launch {
                numberList = phoneDirViewModel.setPhoneList(this@PhoneDirActivity)
            }.join()
            phoneDirViewModel.confirmPhoneList(numberList)
        }

        val phoneList = phoneDirViewModel.phoneList.value?.toMutableList()!!

        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_dir)
        binding.lifecycleOwner = this
        binding.activity = this
        phoneDirAdapter = PhoneDirAdapter(phoneList, phoneViewModel, this)

        binding.phoneDirRecyclerView.apply {

            adapter = phoneDirAdapter
            layoutManager = LinearLayoutManager(this@PhoneDirActivity)
        }
    }

    override fun onResume() {
        super.onResume()

        if(phoneDirAdapter.selectedItem != -1) {

            modifyPhoneListBlocked()

            phoneDirAdapter.notifyItemChanged(phoneDirAdapter.selectedItem)
            phoneDirAdapter.selectedItem = -1
        }
    }

    private fun modifyPhoneListBlocked() {
        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                phoneDirAdapter.phoneList[phoneDirAdapter.selectedItem].blocked =
                    phoneDirViewModel.updatePhone(phoneDirAdapter.phoneList[phoneDirAdapter.selectedItem].number)
            }.join()
        }
    }


    fun addOrCommitListener() {
        val button = binding.addOrCommitButton

        when(phoneDirAdapter.addBtnActivated) {
            true ->  {
                button.background = ContextCompat.getDrawable(this, R.drawable.baseline_add_24)
                phoneDirAdapter.addBtnActivated  = false
            }
            false -> {
                button.background = ContextCompat.getDrawable(this, R.drawable.baseline_check_24)
                phoneDirAdapter.addBtnActivated  = true
            }
        }
        phoneDirAdapter.notifyDataSetChanged()

    }
}