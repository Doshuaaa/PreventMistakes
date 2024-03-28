package com.example.preventmistakes.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.preventmistakes.PhoneDirEntity
import com.example.preventmistakes.activity.PhoneDetailsActivity
import com.example.preventmistakes.databinding.ViewHolderPhoneDirBinding
import com.example.preventmistakes.model.Phone
import com.example.preventmistakes.view_model.PhoneViewModel

class PhoneDirAdapter(
    val phoneList: MutableList<Phone>,
    private val phoneViewModel: PhoneViewModel,
    private val context: Context)
    : RecyclerView.Adapter<PhoneDirAdapter.ViewHolder>() {

    private lateinit var binding: ViewHolderPhoneDirBinding
    var addBtnActivated = false
    var selectedItem = -1

    inner class ViewHolder(private val binding: ViewHolderPhoneDirBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.viewHolder = this
            binding.data = null
        }

        lateinit var phone: Phone
        private var position = 0
        fun onBind(phone: Phone, position: Int) {

            this.position = position
            this.phone = phone
            binding.data = phone
            binding.blocked = phone.blocked

            setBlockedTextView()
        }

        private fun setBlockedTextView() {
            if(addBtnActivated) {

                if(!phone.blocked) {
                    binding.showCheckBox = true
                }
            } else {
                if(binding.blockCheckBox.isChecked) {
                    phoneViewModel.blockPhone(PhoneDirEntity(phone.number, phone.name))
                    binding.blocked = true               // xml에 표시하기 위함
                    phoneList[position].blocked = true   // details activity에 전달하기 위함
                    binding.blockCheckBox.isChecked = false
                }
                binding.showCheckBox = false
            }
        }

        fun layoutListener() {

            selectedItem = position
            val intent = Intent(context, PhoneDetailsActivity::class.java)
            intent.putExtra("selected_phone", Phone(phone.name, phone.number, phone.blocked))
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ViewHolderPhoneDirBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return phoneList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val phone = phoneList[position]
        holder.onBind(phone, position)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}