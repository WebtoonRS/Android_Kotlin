package com.example.webtoon_project.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.webtoon_project.Retrofit.INodeJS
import com.example.webtoon_project.databinding.ItemWebtoonBinding

class WebtoonAdapter : ListAdapter<INodeJS.Webtoon, WebtoonAdapter.WebtoonViewHolder>(WebtoonDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebtoonViewHolder {
        val binding = ItemWebtoonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WebtoonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WebtoonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WebtoonViewHolder(private val binding: ItemWebtoonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(webtoon: INodeJS.Webtoon) {
            binding.textViewTitle.text = webtoon.title
            // 썸네일 이미지 로딩 로직 추가 (예: Glide 또는 Picasso 사용)
        }
    }

    class WebtoonDiffCallback : DiffUtil.ItemCallback<INodeJS.Webtoon>() {
        override fun areItemsTheSame(oldItem: INodeJS.Webtoon, newItem: INodeJS.Webtoon): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: INodeJS.Webtoon, newItem: INodeJS.Webtoon): Boolean {
            return oldItem == newItem
        }
    }
}
