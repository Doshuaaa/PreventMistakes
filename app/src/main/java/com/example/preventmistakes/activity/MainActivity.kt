package com.example.preventmistakes.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.preventmistakes.R
import com.example.preventmistakes.databinding.ActivityMainBinding
import com.example.preventmistakes.view_model.MainViewModel

const val REQUEST_PERMISSIONS = 1

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermission()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainViewModel = mainViewModel
        binding.lifecycleOwner = this
        binding.activity = this
        binding.text1.visibility
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
}