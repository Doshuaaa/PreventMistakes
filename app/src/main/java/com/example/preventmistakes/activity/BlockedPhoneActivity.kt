package com.example.preventmistakes.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.preventmistakes.R
import com.example.preventmistakes.adapter.BlockedPhoneAdapter
import com.example.preventmistakes.databinding.ActivityBlockedPhoneBinding
import com.example.preventmistakes.view_model.BlockedPhoneViewModel
import com.example.preventmistakes.view_model.BlockedPhoneViewModelFactory

class BlockedPhoneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlockedPhoneBinding
    private val blockedPhoneViewModel: BlockedPhoneViewModel by viewModels { BlockedPhoneViewModelFactory(application) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_blocked_phone)

        binding.apply {
            blockedPhoneRecyclerView.adapter = BlockedPhoneAdapter(blockedPhoneViewModel.blockedPhoneList)
            blockedPhoneRecyclerView.layoutManager = LinearLayoutManager(this@BlockedPhoneActivity)
        }
    }
}