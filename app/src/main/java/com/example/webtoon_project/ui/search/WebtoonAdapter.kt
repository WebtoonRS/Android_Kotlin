//package com.example.webtoon_project.ui.search
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.webtoon_project.Retrofit.INodeJS
//import com.example.webtoon_project.databinding.ItemSearchBinding
//
//class WebtoonAdapter : ListAdapter<INodeJS.Webtoon, WebtoonAdapter.WebtoonViewHolder>(WebtoonDiffCallback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebtoonViewHolder {
//        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return WebtoonViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: WebtoonViewHolder, position: Int) {
//        holder.bind(getItem(position))
//    }
//
//    class WebtoonViewHolder(private val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(webtoon: INodeJS.Webtoon) {
//            binding.apply {
//                textViewTitle.text = webtoon.title
//                textViewAuthor.text = webtoon.author ?: "작가미상"
//                ratingBarWebtoon.rating = webtoon.rating?.toFloat() ?: 0f
//
//                // 썸네일 이미지 로드
//                Glide.with(root.context)
//                    .load(webtoon.thumbnail_link)
//                    .into(imageViewThumbnail)
//            }
//        }
//    }
//
//    class WebtoonDiffCallback : DiffUtil.ItemCallback<INodeJS.Webtoon>() {
//        override fun areItemsTheSame(oldItem: INodeJS.Webtoon, newItem: INodeJS.Webtoon): Boolean {
//            return oldItem.title == newItem.title
//        }
//
//        override fun areContentsTheSame(oldItem: INodeJS.Webtoon, newItem: INodeJS.Webtoon): Boolean {
//            return oldItem.title == newItem.title &&
//                   oldItem.thumbnail_link == newItem.thumbnail_link &&
//                   oldItem.author == newItem.author &&
//                   oldItem.rating == newItem.rating
//        }
//    }
//}