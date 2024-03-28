package com.example.preventmistakes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.preventmistakes.PhoneDirEntity
import com.example.preventmistakes.databinding.ViewHolderBlockedPhoneBinding

class BlockedPhoneAdapter(private val blockedList: List<PhoneDirEntity>)
    : RecyclerView.Adapter<BlockedPhoneAdapter.ViewHolder>() {

    private lateinit var binding: ViewHolderBlockedPhoneBinding

    inner class ViewHolder(binding: ViewHolderBlockedPhoneBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(phone: PhoneDirEntity) {

            binding.data = phone
            binding.viewHolder = this
        }

        fun layoutListener() {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding = ViewHolderBlockedPhoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return blockedList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(blockedList[position])
    }
}