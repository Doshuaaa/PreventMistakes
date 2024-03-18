package com.example.preventmistakes

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.preventmistakes.databinding.ActivityMainBinding

const val REQUEST_PERMISSIONS = 1

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        checkPermission()
        mainViewModel.permissionVisibleList.observe(this, Observer {
            binding.text1.visibility = mainViewModel.getTextVisibility(0)
            binding.text2.visibility = mainViewModel.getTextVisibility(1)
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainViewModel = mainViewModel
        binding.lifecycleOwner = this
        val a = mainViewModel.permissionVisibleList?.value?.get(0)
        binding.text1.visibility
    }

    private fun checkPermission() {

        val permissions = mutableMapOf<String, String>()
        permissions["phone"] = Manifest.permission.ANSWER_PHONE_CALLS
        permissions["read"] = Manifest.permission.READ_CONTACTS

        val denied = permissions.count { ContextCompat.checkSelfPermission(this, it.value ) == PackageManager.PERMISSION_DENIED }

        if(denied > 0) {
            requestPermissions(permissions.values.toTypedArray(), REQUEST_PERMISSIONS)
        } else {
            mainViewModel.setPermissionVisibleList(arrayOf(View.GONE, View.GONE))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val array = Array(grantResults.size) { i ->
            if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
        mainViewModel.setPermissionVisibleList(array)
    }

    fun goToPhoneDirActivity() {
        val intent = Intent(this, PhoneDirActivity::class.java)
        startActivity(intent)
    }

    fun goToBlockedPhoneActivity() {

    }
}