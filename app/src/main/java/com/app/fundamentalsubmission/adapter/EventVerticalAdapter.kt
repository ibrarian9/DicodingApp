package com.app.fundamentalsubmission.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.fundamentalsubmission.databinding.ListEventBinding
import com.app.fundamentalsubmission.di.models.ListEventsItem
import com.app.fundamentalsubmission.ui.detail.DetailActivity
import com.bumptech.glide.Glide

class EventVerticalAdapter: ListAdapter<ListEventsItem, EventVerticalAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(ListEventBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ListEventBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListEventsItem) {
            binding.apply {
                Glide.with(itemView.context).load(item.imageLogo).into(ivPhoto)
                tvJudul.text = item.name
                tvDesc.text = item.summary
            }
            itemView.setOnClickListener {
                val i = Intent(it.context, DetailActivity::class.java)
                i.putExtra(DetailActivity.DETAIL_ID, item.id)
                it.context.startActivity(i)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}