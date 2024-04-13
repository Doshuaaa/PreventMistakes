package com.example.preventmistakes.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.preventmistakes.R
import com.example.preventmistakes.adapter.BlockedPhoneByDirAdapter
import com.example.preventmistakes.adapter.BlockedPhoneByNumberAdapter
import com.example.preventmistakes.databinding.ActivityBlockedPhoneBinding
import com.example.preventmistakes.view_model.BlockedPhoneViewModel
import com.example.preventmistakes.view_model.BlockedPhoneViewModelFactory
import com.example.preventmistakes.view_model.MainViewModel

class BlockedPhoneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlockedPhoneBinding
    private val blockedPhoneViewModel: BlockedPhoneViewModel by viewModels { BlockedPhoneViewModelFactory(application) }
    private lateinit var blockedPhoneByDirAdapter: BlockedPhoneByDirAdapter
    private lateinit var blockedPhoneByNumberAdapter: BlockedPhoneByNumberAdapter

    private val mainViewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_blocked_phone)
        blockedPhoneByDirAdapter = BlockedPhoneByDirAdapter(blockedPhoneViewModel.blockedPhoneByDirList, this)
        blockedPhoneByNumberAdapter = BlockedPhoneByNumberAdapter(blockedPhoneViewModel.blockedPhoneByNumberList.toMutableList(), blockedPhoneViewModel, this)
        binding.apply {
            blockedPhoneRecyclerView.adapter = blockedPhoneByDirAdapter
            blockedPhoneRecyclerView.layoutManager = LinearLayoutManager(this@BlockedPhoneActivity)
            blockedPhoneRecyclerView.addItemDecoration(DividerItemDecoration(this@BlockedPhoneActivity, VERTICAL))

            blockedPhoneByNumberRecyclerView.adapter = blockedPhoneByNumberAdapter
            blockedPhoneByNumberRecyclerView.layoutManager = LinearLayoutManager(this@BlockedPhoneActivity)
            blockedPhoneByNumberRecyclerView.addItemDecoration(DividerItemDecoration(this@BlockedPhoneActivity, VERTICAL))
        }
    }

    override fun onResume() {
        super.onResume()
        if(blockedPhoneByDirAdapter.selectedItem != -1) {
            blockedPhoneViewModel.updateBlockedPhone(blockedPhoneByDirAdapter, blockedPhoneByDirAdapter.selectedItem)
        }

        val isServiceRunning = mainViewModel.isServiceRunning(this)
        mainViewModel.setIsServiceRunning(isServiceRunning)
        if(isServiceRunning) {

            window.statusBarColor = ContextCompat.getColor(this, R.color.red)
        } else {
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }
    }

}