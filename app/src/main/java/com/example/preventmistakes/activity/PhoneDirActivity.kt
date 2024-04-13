package com.example.preventmistakes.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.preventmistakes.PhoneDirEntity
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
    private val phoneList: MutableList<Phone> by lazy { phoneDirViewModel.phoneList.value?.toMutableList()!! }
    private val prefs: SharedPreferences by lazy { getSharedPreferences("changeable_data", Context.MODE_PRIVATE) }
    private val phoneViewModel by lazy { PhoneViewModel(this.application) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    //    Current.currWindow = window
        initPhoneList()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_dir)
        binding.lifecycleOwner = this
        binding.activity = this
        phoneDirAdapter = PhoneDirAdapter(phoneList, phoneDirViewModel.checkList, this)


        binding.phoneDirRecyclerView.apply {

            adapter = phoneDirAdapter
            layoutManager = LinearLayoutManager(this@PhoneDirActivity)
            addItemDecoration(DividerItemDecoration(this@PhoneDirActivity, VERTICAL))
        }
    }

    override fun onResume() {
        super.onResume()

        val index = prefs.getInt("changeable_phone_index", -1)

        if(index != -1) {
            modifyPhoneListBlocked(index)
            phoneDirAdapter.notifyItemChanged(index)
            prefs.edit().putInt("changeable_phone_index", -1).apply()
        }
    }

    private fun initPhoneList() {
        runBlocking {
            var numberList = arrayListOf<Phone>()
            CoroutineScope(Dispatchers.IO).launch {
                numberList = phoneDirViewModel.setPhoneList(this@PhoneDirActivity)
            }.join()
            phoneDirViewModel.confirmPhoneList(numberList)
        }
    }

    private fun modifyPhoneListBlocked(index: Int) {
        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                phoneDirAdapter.phoneList[index].blocked =
                    phoneDirViewModel.updatePhone(phoneDirAdapter.phoneList[index].number)
            }.join()
        }
    }

    fun addOrCommitListener() {
        val button = binding.addOrCommitButton

        when(phoneDirAdapter.addBtnActivated) {
            true ->  {
                blockedPhoneByChecking()
                button.background = ContextCompat.getDrawable(this, R.drawable.baseline_checklist_24)
                phoneDirAdapter.addBtnActivated  = false
            }
            false -> {
                button.background = ContextCompat.getDrawable(this, R.drawable.baseline_check_24)
                phoneDirAdapter.addBtnActivated  = true
            }
        }
        phoneDirAdapter.notifyDataSetChanged()
    }

    private fun blockedPhoneByChecking() {
        for(i in phoneDirViewModel.checkList.indices) {
            if(phoneDirViewModel.checkList[i]) {
                phoneViewModel.blockPhone(PhoneDirEntity(phoneDirViewModel.phoneList.value!![i].number, phoneDirViewModel.phoneList.value!![i].name) )
            }
        }
        phoneDirViewModel.checkList.fill(false)
    }

    fun goToSearchActivity() {
        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra("phone_dir_list", phoneList.toTypedArray())

        startActivity(intent)
    }
}