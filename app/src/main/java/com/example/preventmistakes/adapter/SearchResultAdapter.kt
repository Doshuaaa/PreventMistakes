package com.example.preventmistakes.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.preventmistakes.activity.PhoneDetailsActivity
import com.example.preventmistakes.databinding.ViewHolderResultPhoneBinding
import com.example.preventmistakes.model.Phone
import com.example.preventmistakes.model.PhonePosition

class SearchResultAdapter(var list: List<PhonePosition>, private val context: Context)
    : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    var selectedPosition = -1
    private val prefs = context.getSharedPreferences("changeable_data", Context.MODE_PRIVATE)
    inner class ViewHolder(private val binding: ViewHolderResultPhoneBinding): RecyclerView.ViewHolder(binding.root) {

        lateinit var phone: Phone
        private var position = -1
        fun onBind(phonePosition: PhonePosition, position: Int) {
            this.position = position
            phone = phonePosition.phone
            binding.apply {
                data = phone
                viewHolder = this@ViewHolder
            }
        }

        fun layoutListener() {
            prefs.edit().putInt("changeable_phone_index", position).apply()
            selectedPosition = position
            val intent = Intent(context, PhoneDetailsActivity::class.java)
            intent.putExtra("selected_phone", Phone(phone.name, phone.number, phone.blocked, phone.index))
            context.startActivity(intent)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewHolderResultPhoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position], position)
    }
}