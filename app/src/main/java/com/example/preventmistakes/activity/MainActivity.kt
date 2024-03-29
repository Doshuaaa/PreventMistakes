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
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.preventmistakes.R
import com.example.preventmistakes.databinding.ActivityMainBinding
import com.example.preventmistakes.service.BlockingCallsService
import com.example.preventmistakes.view_model.MainViewModel
import com.example.preventmistakes.view_model.PhoneViewModel
import com.example.preventmistakes.view_model.PhoneViewModelFactory

const val REQUEST_PERMISSIONS = 1

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val phoneViewModel: PhoneViewModel by viewModels { PhoneViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermission()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainViewModel = mainViewModel

        binding.lifecycleOwner = this
        binding.activity = this

        mainViewModel.isServiceRunning.observe(this, Observer {
            if(it) {
                val filter = IntentFilter("android.intent.action.NEW_OUTGOING_CALL")
                registerReceiver(CallReceiver(), filter)
                val intent = Intent(this, BlockingCallsService::class.java)
                ContextCompat.startForegroundService(this, intent)
            }
        })
    }

    fun onSwitchListener() {
        mainViewModel.setIsServiceRunning(binding.preventSwitch.isChecked)
    }

    private fun checkPermission() {

        val permissions = mutableMapOf<String, String>()
        permissions["phone"] = Manifest.permission.ANSWER_PHONE_CALLS
        permissions["read"] = Manifest.permission.READ_CONTACTS

        val denied = permissions.count { ContextCompat.checkSelfPermission(this, it.value ) == PackageManager.PERMISSION_DENIED }

        if(denied > 0) {
            requestPermissions(permissions.values.toTypedArray(), REQUEST_PERMISSIONS)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for (i in grantResults.indices) {

            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                when (i) {
                    0 -> mainViewModel.setPermissionVisibility("phone", View.VISIBLE)
                    1 -> mainViewModel.setPermissionVisibility("read", View.VISIBLE)
                }
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
}