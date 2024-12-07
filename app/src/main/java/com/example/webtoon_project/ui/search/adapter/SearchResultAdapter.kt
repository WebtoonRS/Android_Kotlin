package com.example.webtoon_project.ui.search.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.webtoon_project.Retrofit.INodeJS
import com.example.webtoon_project.R
import com.example.webtoon_project.databinding.ItemWebtoonListBinding

class SearchResultAdapter : ListAdapter<INodeJS.Webtoon, SearchResultAdapter.ViewHolder>(WebtoonDiffCallback()) {

    private var onItemClickListener: ((INodeJS.Webtoon) -> Unit)? = null

    fun setOnItemClickListener(listener: (INodeJS.Webtoon) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemWebtoonListBinding.inflate(
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

    class ViewHolder(private val binding: ItemWebtoonListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(webtoon: INodeJS.Webtoon) {
            binding.webtoonName.text = webtoon.title
            binding.webtoonInfo.text = webtoon.synopsis

            // 웹툰 제목을 파일명 형식으로 변환
            val fileName = webtoon.title?.let { title ->
                title.replace(Regex("[\\\\/*?:\"<>|]"), "")
                    .replace(" ", "_")
            }

            try {
                // Assets에서 이미지 로드
                Glide.with(binding.root)
                    .load("file:///android_asset/thumbnails/${fileName}.jpg")
                    .error(R.drawable.error_image)
                    .into(binding.webtoonImage)
            } catch (e: Exception) {
                Log.e("SearchResultAdapter", "이미지 로드 실패: $fileName", e)
            }
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
