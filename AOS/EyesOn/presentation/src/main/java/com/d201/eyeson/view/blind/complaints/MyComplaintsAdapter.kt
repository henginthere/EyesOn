package com.d201.eyeson.view.blind.complaints

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.d201.domain.model.Complaints
import com.d201.eyeson.databinding.ItemComplaintsVerticalBlindBinding
import com.d201.eyeson.view.blind.BlindComplaintsClickListener

private const val TAG = "MyComplaintsAdapter"

class MyComplaintsAdapter(private val blindComplaintsClickListener: BlindComplaintsClickListener) :
    PagingDataAdapter<Complaints, MyComplaintsAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemComplaintsVerticalBlindBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(complaints: Complaints) {
            binding.apply {
                layoutComplaints.setOnClickListener {
                    blindComplaintsClickListener.onClick(complaints)
                }
                when (complaints.state) {
                    "PROGRESS_IN" -> {
                        tvComplaintsStatusProcessing.visibility = View.VISIBLE
                    }
                    "RETURN" -> {
                        tvComplaintsStatusReturn.visibility = View.VISIBLE
                    }
                    "REGIST_DONE" -> {
                        tvComplaintsStatusProcessing.visibility = View.VISIBLE
                    }
                    "PROGRESS_DONE" -> {
                        tvComplaintsStatusDone.visibility = View.VISIBLE
                    }
                }
                data = complaints
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemComplaintsVerticalBlindBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Complaints>() {
            override fun areItemsTheSame(oldItem: Complaints, newItem: Complaints) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Complaints, newItem: Complaints) =
                oldItem.seq == newItem.seq
        }
    }
}