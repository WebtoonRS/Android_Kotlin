package com.example.webtoon_project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.webtoon_project.databinding.ItemWebtoonBinding

class WebtoonAdapter(
    private var webtoons: List<WebtoonItem>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<WebtoonAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemWebtoonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(webtoon: WebtoonItem, onItemClick: (Int) -> Unit) {
            binding.apply {
                tvTitle.text = webtoon.title
                tvAuthor.text = webtoon.author

                // 썸네일 이미지 로딩
                Glide.with(ivThumbnail.context)
                    .load(webtoon.thumbnailUrl)
                    // .placeholder(R.drawable.placeholder_image) // 로딩 중 표시할 이미지
                    // .error(R.drawable.error_image) // 에러 시 표시할 이미지
                    .centerCrop()
                    .into(ivThumbnail)

                root.setOnClickListener {
                    onItemClick(webtoon.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWebtoonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(webtoons[position], onItemClick)
    }

    override fun getItemCount() = webtoons.size

    fun updateWebtoons(newWebtoons: List<WebtoonItem>) {
        webtoons = newWebtoons
        notifyDataSetChanged()
    }
}

data class WebtoonItem(
    val id: Int,
    val title: String,
    val author: String,
    val thumbnailUrl: String,
    val synopsis: String
)
