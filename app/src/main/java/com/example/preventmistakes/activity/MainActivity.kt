package com.example.preventmistakes.activity

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.TelecomManager
import android.view.MenuItem
import android.view.Window
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.preventmistakes.R
import com.example.preventmistakes.databinding.ActivityMainBinding
import com.example.preventmistakes.service.BlockingCallsService
import com.example.preventmistakes.service.SERVICE_COMMAND
import com.example.preventmistakes.service.ServiceState
import com.example.preventmistakes.view_model.MainViewModel
import com.example.preventmistakes.view_model.PhoneViewModel
import com.example.preventmistakes.view_model.PhoneViewModelFactory
import java.lang.IllegalArgumentException

const val REQUEST_PERMISSIONS = 1

lateinit var currWindow: Window

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val phoneViewModel: PhoneViewModel by viewModels { PhoneViewModelFactory(application) }
    private lateinit var callReceiver: CallReceiver

    companion object {
        lateinit var viewModel: MainViewModel
    }

    private val onBackPressedCallBack: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer((GravityCompat.START))
            } else {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    val stopActionPrefs by lazy { getSharedPreferences("stop_action_flag", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = mainViewModel

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.mainViewModel = mainViewModel

        binding.lifecycleOwner = this
        binding.activity = this


        setSupportActionBar(binding.toolBar)
        with(supportActionBar) {
            this?.setDisplayShowTitleEnabled(false)
            this?.setDisplayHomeAsUpEnabled(true)
            this?.setHomeAsUpIndicator(R.drawable.baseline_dehaze_24)
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallBack)
        binding.drawerLayout.close()
        callReceiver = CallReceiver()

        mainViewModel.isServiceRunning.observeForever(Observer {
            val intent = Intent(this, BlockingCallsService::class.java)
            if(it) {
                val filter = IntentFilter("android.intent.action.NEW_OUTGOING_CALL")
                registerReceiver(callReceiver, filter)
                intent.putExtra(SERVICE_COMMAND, ServiceState.START)
                ContextCompat.startForegroundService(this, intent)
            } else {
                try {
                    unregisterReceiver(callReceiver)     // 맨처음 isServiceRunning을 초기화 하는 과정에서 호출되어 IllegalArgumentException 발생 때문에 try catch 사용
                    intent.putExtra(SERVICE_COMMAND, ServiceState.STOP)
                    ContextCompat.startForegroundService(this, intent)
                } catch (_: IllegalArgumentException) { }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        checkPermission()

        val isServiceRunning = mainViewModel.isServiceRunning(this)
        mainViewModel.setIsServiceRunning(isServiceRunning)
        if(isServiceRunning) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.red)
        } else {
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }
    }

    override fun onStop() {
        super.onStop()
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START, false)
        }
    }

    fun onSwitchListener() {
        val isChecked = binding.preventSwitch.isChecked
        mainViewModel.setIsServiceRunning(isChecked)
        if(isChecked) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.red)
        } else {
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }
    }

    private fun checkPermission() {
        val essentialPermissions = hashMapOf<String, String>()
        essentialPermissions["PROCESS_OUTGOING_CALLS"] = Manifest.permission.PROCESS_OUTGOING_CALLS
        essentialPermissions["ANSWER_PHONE_CALLS"] = Manifest.permission.ANSWER_PHONE_CALLS
        essentialPermissions["READ_CONTACTS"] = Manifest.permission.READ_CONTACTS

        for(permission in essentialPermissions) {
            if(ContextCompat.checkSelfPermission(this, permission.value) == PackageManager.PERMISSION_DENIED) {
                val intent = Intent(this, PermissionCheckActivity::class.java)
                finish()
                startActivity(intent)
                return
            }
        }
    }

    fun goToPhoneDirActivity() {
        val intent = Intent(this, PhoneDirActivity::class.java)
        startActivity(intent)
    }

    fun goToBlockedPhoneActivity() {
        val intent = Intent(this, BlockedPhoneActivity::class.java)
        startActivity(intent)
    }

    fun goToBlockingByInputNumbActivity() {
        val intent = Intent(this, BlockingByInputNumActivity::class.java)
        startActivity(intent)
    }

    inner class CallReceiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent) {

            if(intent.action == "android.intent.action.NEW_OUTGOING_CALL") {
                val extras = intent.extras
                val num = extras?.getString(Intent.EXTRA_PHONE_NUMBER)

                if(phoneViewModel.isBlocked(num?.replace("-", "")!!)) {
                    val telecomManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager

                    if (ActivityCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.ANSWER_PHONE_CALLS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        telecomManager.endCall()
                    }
                }
            }
        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            android.R.id.home -> binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

}

