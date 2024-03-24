package com.example.preventmistakes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.preventmistakes.databinding.ViewHolderPhoneDirBinding
import com.example.preventmistakes.model.Phone
import com.example.preventmistakes.view_model.PhoneViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PhoneDirAdapter(private val phoneList: List<Phone>, private val phoneViewModel: PhoneViewModel)
    : RecyclerView.Adapter<PhoneDirAdapter.ViewHolder>() {

    private lateinit var binding: ViewHolderPhoneDirBinding
    inner class ViewHolder(private val binding: ViewHolderPhoneDirBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.viewHolder = this
            binding.data = null
        }

        fun onBind(phone: Phone) {
            binding.data = phone
        }

        fun isBlocked(phone: Phone) {
            val phone = binding.data

            if(phone != null) {
                var visibility = View.VISIBLE

                if(phone.blocked) {
                    binding.blockedTextView.visibility = View.VISIBLE
                } else {
                    binding.blockedTextView.visibility = View.GONE
                }
            }

//            runBlocking {
//                CoroutineScope(Dispatchers.IO).launch {
//                    if(phone != null) {
//                        if(phoneViewModel.isBlocked(phone.number)) {
//                            visibility = View.VISIBLE
//                            //binding.blockedTextView.visibility = View.VISIBLE
//                        } else {
//                            visibility = View.GONE
//                            //binding.blockedTextView.visibility = View.GONE
//                        }
//                    }
//                    else visibility = View.GONE
//                }
//            }
//            binding.blockedTextView.visibility = visibility
            //return visibility
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
        holder.isBlocked(phone)
        holder.onBind(phone)
    }
}