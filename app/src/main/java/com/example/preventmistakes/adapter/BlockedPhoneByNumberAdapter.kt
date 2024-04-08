package com.example.preventmistakes.adapter

import android.content.Context
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.preventmistakes.PhoneDirEntity
import com.example.preventmistakes.databinding.ViewHolderPhoneByNumberBinding
import com.example.preventmistakes.view_model.BlockedPhoneViewModel
import java.util.Locale

class BlockedPhoneByNumberAdapter(
    private val blockedList: MutableList<PhoneDirEntity>,
    private val blockedPhoneViewModel: BlockedPhoneViewModel,
    private val context: Context
)
    : RecyclerView.Adapter<BlockedPhoneByNumberAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ViewHolderPhoneByNumberBinding): RecyclerView.ViewHolder(binding.root) {

        lateinit var phone: PhoneDirEntity
        private var position = -1
        fun onBind(phone: PhoneDirEntity, position: Int) {
            this.phone = phone
            this.position = position
            binding.viewHolder = this
            binding.data = phone
        }

        fun layoutListener() {
            val dlg = AlertDialog.Builder(context)
            with(dlg) {
                setMessage("${PhoneNumberUtils.formatNumber(phone.phoneNumber, Locale.getDefault().country)} 번호를 발신 차단 해제할까요?")
                setPositiveButton("확인"
                ) { _, _ ->
                    blockedPhoneViewModel.unBlockPhone(phone)
                    blockedList.removeAt(position)
                    notifyDataSetChanged()
                }
                setNegativeButton("취소", null)
                show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ViewHolderPhoneByNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return blockedList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(blockedList[position], position)
    }
}