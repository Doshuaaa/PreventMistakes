package com.example.preventmistakes.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.preventmistakes.R
import com.example.preventmistakes.databinding.ActivityPermissionCheckBinding
import com.example.preventmistakes.view_model.PermissionViewModel

class PermissionCheckActivity : AppCompatActivity() {

    private val viewModel: PermissionViewModel by viewModels()
    private lateinit var binding: ActivityPermissionCheckBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_permission_check)

        with(binding) {
            viewModel = this.viewModel
            lifecycleOwner = this@PermissionCheckActivity
        }
        checkPermission()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun checkPermission() {
        val deniedPermission: ArrayList<String> = arrayListOf()
        val essentialPermissions = hashMapOf<String, String>()
        val map = hashMapOf<String, Int>()
        essentialPermissions["PROCESS_OUTGOING_CALLS"] = Manifest.permission.PROCESS_OUTGOING_CALLS
        essentialPermissions["ANSWER_PHONE_CALLS"] = Manifest.permission.ANSWER_PHONE_CALLS
        essentialPermissions["READ_CONTACTS"] = Manifest.permission.READ_CONTACTS

        for(permission in essentialPermissions) {
            if(ContextCompat.checkSelfPermission(this, permission.value) == PackageManager.PERMISSION_DENIED) {
                deniedPermission.add(permission.value)
                map[permission.key] = PackageManager.PERMISSION_DENIED
            } else {
                map[permission.key] = PackageManager.PERMISSION_GRANTED
            }
        }

        viewModel.setPermissionMap(map)

        if(deniedPermission.size > 0) {
            requestPermissions(deniedPermission.toTypedArray(), REQUEST_PERMISSIONS)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for(i in grantResults.indices) {
            if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {

            } else {

            }
        }
    }
}