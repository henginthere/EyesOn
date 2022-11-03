package com.d201.eyeson.view.angel

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.d201.domain.model.Complaints
import com.d201.eyeson.databinding.ItemComplaintsBinding

private const val TAG ="AngelMainAdapter"
class AngelMainAdapter: PagingDataAdapter<Complaints, AngelMainAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemComplaintsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(complaints: Complaints){
            binding.data = complaints
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if(item != null){
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemComplaintsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
