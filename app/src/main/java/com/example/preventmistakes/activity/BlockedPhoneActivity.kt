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
    private lateinit var blockedPhoneAdapter: BlockedPhoneAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_blocked_phone)
        blockedPhoneAdapter = BlockedPhoneAdapter(blockedPhoneViewModel.blockedPhoneList, this@BlockedPhoneActivity)
        binding.apply {
            blockedPhoneRecyclerView.adapter = blockedPhoneAdapter
            blockedPhoneRecyclerView.layoutManager = LinearLayoutManager(this@BlockedPhoneActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        if(blockedPhoneAdapter.selectedItem != -1) {
            blockedPhoneViewModel.updateBlockedPhone(blockedPhoneAdapter, blockedPhoneAdapter.selectedItem)
        }
    }
}