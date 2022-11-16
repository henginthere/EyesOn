package com.d201.eyeson.view.blind.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d201.domain.model.Noti
import com.d201.eyeson.databinding.ItemNotificationBinding
import com.d201.eyeson.view.blind.NotiClickListener

private const val TAG = "BlindNotiAdapter"

class BlindNotiAdapter(private val notiClickListener: NotiClickListener) :
    ListAdapter<Noti, BlindNotiAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(noti: Noti, position: Int) {
            binding.noti = noti
            binding.ivDelete.setOnClickListener { notiClickListener.onClick(noti, position) }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Noti>() {
            override fun areItemsTheSame(oldItem: Noti, newItem: Noti) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Noti, newItem: Noti) =
                oldItem.seq == newItem.seq
        }
    }
}