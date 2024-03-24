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
import com.example.preventmistakes.view_model.PhoneDirViewModel
import com.example.preventmistakes.view_model.PhoneDirViewModelFactory
import com.example.preventmistakes.view_model.PhoneViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PhoneDirActivity : AppCompatActivity() {


    private lateinit var binding: ActivityPhoneDirBinding
    private val phoneDirViewModel: PhoneDirViewModel by viewModels{ PhoneDirViewModelFactory(application)}
    //private val phoneViewModel: PhoneViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val phoneViewModel = PhoneViewModel(this.application)

        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                phoneDirViewModel.setPhoneList(this@PhoneDirActivity)
            }.join()
            delay(3000)
        }

        val phoneList = phoneDirViewModel.phoneList.value!!

        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_dir)

        binding.phoneDirRecyclerView.apply {

            adapter = PhoneDirAdapter(phoneList, phoneViewModel)
            layoutManager = LinearLayoutManager(this@PhoneDirActivity)
        }


    }

    fun addOrCommitListener() {
        val button = binding.addOrCommitButton
        when(button.background) {
            ContextCompat.getDrawable(this, R.drawable.baseline_add_24) -> {

            }
            ContextCompat.getDrawable(this, R.drawable.baseline_check_24) -> {

            }
        }
    }
}