package com.example.preventmistakes.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.preventmistakes.PhoneDirEntity
import com.example.preventmistakes.activity.PhoneDetailsActivity
import com.example.preventmistakes.databinding.ViewHolderBlockPhoneByDirBinding
import com.example.preventmistakes.model.Phone

class BlockedPhoneByDirAdapter(
    private val blockedList: MutableList<PhoneDirEntity>,
    private val context: Context
)
    : RecyclerView.Adapter<BlockedPhoneByDirAdapter.ViewHolder>() {

    private lateinit var binding: ViewHolderBlockPhoneByDirBinding
    var selectedItem = -1


    inner class ViewHolder(val binding: ViewHolderBlockPhoneByDirBinding) : RecyclerView.ViewHolder(binding.root) {

        lateinit var phone: PhoneDirEntity
        private var position = 0
        fun onBind(phone: PhoneDirEntity, position: Int) {


            this.phone = phone
            this.position = position
            selectedItem = position
            binding.data = phone
            binding.viewHolder = this
        }

        fun layoutListener() {
            selectedItem = position
            val intent = Intent(context, PhoneDetailsActivity::class.java)
            intent.putExtra("selected_phone", Phone(phone.name, phone.phoneNumber, true, 0))
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding = ViewHolderBlockPhoneByDirBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return blockedList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.name.text = blockedList[position].name
        holder.binding.number.text = blockedList[position].phoneNumber
        holder.onBind(blockedList[position], position)
    }
}