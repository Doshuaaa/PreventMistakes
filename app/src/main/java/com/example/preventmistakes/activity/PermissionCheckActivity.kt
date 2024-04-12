package com.example.preventmistakes.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.preventmistakes.R
import com.example.preventmistakes.databinding.ActivityPermissionCheckBinding
import com.example.preventmistakes.view_model.PermissionViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class PermissionCheckActivity : AppCompatActivity() {

    private val viewModel: PermissionViewModel by viewModels()
    private lateinit var binding: ActivityPermissionCheckBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_permission_check)

        with(binding) {
            viewModel = this@PermissionCheckActivity.viewModel
            lifecycleOwner = this@PermissionCheckActivity
            activity = this@PermissionCheckActivity
        }
        viewModel.permissionMap.observe(this, Observer {
            viewModel.setVisibleFlag()
        })

    }

    override fun onResume() {
        super.onResume()
        checkPermission()
    }


    private fun checkPermission() {
        val deniedPermission: ArrayList<String> = arrayListOf()
        val essentialPermissions = hashMapOf<String, String>()
        essentialPermissions["PROCESS_OUTGOING_CALLS"] = Manifest.permission.PROCESS_OUTGOING_CALLS
        essentialPermissions["ANSWER_PHONE_CALLS"] = Manifest.permission.ANSWER_PHONE_CALLS
        essentialPermissions["READ_CONTACTS"] = Manifest.permission.READ_CONTACTS
        val map: HashMap<String, Int> = hashMapOf()

        for(permission in essentialPermissions) {
            if(ContextCompat.checkSelfPermission(this, permission.value) == PackageManager.PERMISSION_DENIED) {
                deniedPermission.add(permission.value)
                map[permission.key] = PackageManager.PERMISSION_DENIED
            } else {
                map[permission.key] = PackageManager.PERMISSION_GRANTED
            }
        }
        viewModel.permissionMap.value = map

        if(deniedPermission.size > 0 && viewModel.firstRequestFlag) {
            requestPermissions(deniedPermission.toTypedArray(), REQUEST_PERMISSIONS)
            viewModel.firstRequestFlag = false
        }

        val isNotificationDenied
        = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED

        if(isNotificationDenied) {
            viewModel.notificationPermission.value = PackageManager.PERMISSION_DENIED
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_PERMISSIONS)
        } else {
            viewModel.notificationPermission.value = PackageManager.PERMISSION_GRANTED
        }
    }

    fun goToSetPermission() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:${this.packageName}"))
        startActivity(intent)
    }

    fun goToStart() {
        val intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }
}