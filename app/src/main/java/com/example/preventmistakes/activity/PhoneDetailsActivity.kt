package com.example.preventmistakes.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import com.example.preventmistakes.PhoneDirEntity
import com.example.preventmistakes.R
import com.example.preventmistakes.databinding.ActivityPhoneDetailsBinding
import com.example.preventmistakes.model.Phone
import com.example.preventmistakes.view_model.PhoneDetailsViewModel
import com.example.preventmistakes.view_model.PhoneViewModel
import com.example.preventmistakes.view_model.PhoneViewModelFactory
import java.io.Serializable

class PhoneDetailsActivity : AppCompatActivity() {
    lateinit var phone: Phone
    private lateinit var binding: ActivityPhoneDetailsBinding
    private val phoneViewModel: PhoneViewModel by viewModels { PhoneViewModelFactory(application) }
    private val phoneDetailsViewModel: PhoneDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_details)
        binding.apply {
            activity = this@PhoneDetailsActivity
            lifecycleOwner = this@PhoneDetailsActivity
            viewModel = phoneDetailsViewModel
        }

        phone = intent.intentSerializable("selected_phone", Phone::class.java)!!
        phoneDetailsViewModel.setBlocked(phone.blocked)

    }

    fun blockPhone() {
        phoneViewModel.blockPhone(PhoneDirEntity(phone.number, phone.name))
        phoneDetailsViewModel.setBlocked(true)
    }

    fun unblockPhone() {
        phoneViewModel.unblockPhone(PhoneDirEntity(phone.number, phone.name))
        phoneDetailsViewModel.setBlocked(false)
    }

    private fun <T : Serializable> Intent.intentSerializable(key: String, clazz: Class<T>) : T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.getSerializableExtra(key, clazz)
        } else {
            this.getSerializableExtra(key) as T?
        }
    }
}