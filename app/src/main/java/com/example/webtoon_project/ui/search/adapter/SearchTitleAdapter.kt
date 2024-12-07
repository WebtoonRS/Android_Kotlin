package com.example.webtoon_project.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.webtoon_project.Retrofit.INodeJS
import com.example.webtoon_project.databinding.ItemSearchTitleBinding

class SearchTitleAdapter : ListAdapter<INodeJS.Webtoon, SearchTitleAdapter.ViewHolder>(WebtoonDiffCallback()) {

    private var onItemClickListener: ((INodeJS.Webtoon) -> Unit)? = null

    fun setOnItemClickListener(listener: (INodeJS.Webtoon) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSearchTitleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val webtoon = getItem(position)
        holder.bind(webtoon)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(webtoon)
        }
    }

    class ViewHolder(private val binding: ItemSearchTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(webtoon: INodeJS.Webtoon) {
            binding.titleTextView.text = webtoon.title
        }
    }

    private class WebtoonDiffCallback : DiffUtil.ItemCallback<INodeJS.Webtoon>() {
        override fun areItemsTheSame(oldItem: INodeJS.Webtoon, newItem: INodeJS.Webtoon): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: INodeJS.Webtoon, newItem: INodeJS.Webtoon): Boolean {
            return oldItem == newItem
        }
    }
} 