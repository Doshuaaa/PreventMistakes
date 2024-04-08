package com.example.preventmistakes.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.preventmistakes.R
import com.example.preventmistakes.adapter.BlockedPhoneByDirAdapter
import com.example.preventmistakes.adapter.BlockedPhoneByNumberAdapter
import com.example.preventmistakes.databinding.ActivityBlockedPhoneBinding
import com.example.preventmistakes.view_model.BlockedPhoneViewModel
import com.example.preventmistakes.view_model.BlockedPhoneViewModelFactory

class BlockedPhoneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlockedPhoneBinding
    private val blockedPhoneViewModel: BlockedPhoneViewModel by viewModels { BlockedPhoneViewModelFactory(application) }
    private lateinit var blockedPhoneByDirAdapter: BlockedPhoneByDirAdapter
    private lateinit var blockedPhoneByNumberAdapter: BlockedPhoneByNumberAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_blocked_phone)
        blockedPhoneByDirAdapter = BlockedPhoneByDirAdapter(blockedPhoneViewModel.blockedPhoneByDirList, this)
        blockedPhoneByNumberAdapter = BlockedPhoneByNumberAdapter(blockedPhoneViewModel.blockedPhoneByNumberList.toMutableList(), blockedPhoneViewModel, this)
        binding.apply {
            blockedPhoneRecyclerView.adapter = blockedPhoneByDirAdapter
            blockedPhoneRecyclerView.layoutManager = LinearLayoutManager(this@BlockedPhoneActivity)

            blockedPhoneByNumberRecyclerView.adapter = blockedPhoneByNumberAdapter
            blockedPhoneByNumberRecyclerView.layoutManager = LinearLayoutManager(this@BlockedPhoneActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        if(blockedPhoneByDirAdapter.selectedItem != -1) {
            blockedPhoneViewModel.updateBlockedPhone(blockedPhoneByDirAdapter, blockedPhoneByDirAdapter.selectedItem)
        }
    }
}