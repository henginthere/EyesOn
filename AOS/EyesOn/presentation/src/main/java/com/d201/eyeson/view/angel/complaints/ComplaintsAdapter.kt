package com.d201.eyeson.view.angel.complaints

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.d201.domain.model.Complaints
import com.d201.eyeson.databinding.ItemComplaintsVerticalBinding
import com.d201.eyeson.view.angel.ComplaintsClickListener

private const val TAG = "ComplaintsAdapter"

class ComplaintsAdapter(private val complaintsClickListener: ComplaintsClickListener) :
    PagingDataAdapter<Complaints, ComplaintsAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemComplaintsVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(complaints: Complaints) {
            binding.apply {
                layoutComplaints.setOnClickListener { complaintsClickListener.onClick(complaints.seq) }
                tvComplaintsStatusProcessing.visibility = View.GONE
                tvComplaintsStatusReturn.visibility = View.GONE
                tvComplaintsStatusDone.visibility = View.GONE
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
        val binding = ItemComplaintsVerticalBinding.inflate(
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