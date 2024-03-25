package com.example.preventmistakes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.preventmistakes.PhoneDirEntity
import com.example.preventmistakes.databinding.ViewHolderPhoneDirBinding
import com.example.preventmistakes.model.Phone
import com.example.preventmistakes.view_model.PhoneViewModel

class PhoneDirAdapter(
    private val phoneList: List<Phone>,
    private val phoneViewModel: PhoneViewModel,
)
    : RecyclerView.Adapter<PhoneDirAdapter.ViewHolder>() {

    private lateinit var binding: ViewHolderPhoneDirBinding
    var addBtnActivated = false

    inner class ViewHolder(private val binding: ViewHolderPhoneDirBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.viewHolder = this
            binding.data = null
        }

        fun onBind(phone: Phone) {

            binding.data = phone
            binding.blocked = phone.blocked

            if(addBtnActivated) {

                if(!phone.blocked) {
                    binding.showCheckBox = true
                }
            } else {
                if(binding.blockCheckBox.isChecked) {
                    phoneViewModel.blockPhone(PhoneDirEntity(phone.number, phone.name))
                    binding.blocked = true
                }
                binding.showCheckBox = false
            }
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
        holder.onBind(phone)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}