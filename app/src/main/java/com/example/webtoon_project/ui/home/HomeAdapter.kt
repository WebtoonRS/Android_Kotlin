package com.example.webtoon_project.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.webtoon_project.R
import com.example.webtoon_project.Retrofit.INodeJS

class HomeAdapter(private val webtoonList: List<INodeJS.Webtoon>) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail: ImageView = view.findViewById(R.id.webtoonThumbnail)
        val title: TextView = view.findViewById(R.id.webtoonTitle)

        init {
            view.setOnClickListener {
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val webtoon = webtoonList[position]
        holder.title.text = webtoon.title

        // 웹툰 제목을 파일명 형식으로 변환
        val fileName = webtoon.title?.let { title ->
            title.replace(Regex("[\\\\/*?:\"<>|]"), "")
                .replace(" ", "_")
        }

        try {
            // Assets에서 이미지 로드
            Glide.with(holder.thumbnail.context)
                .load("file:///android_asset/thumbnails/${fileName}.jpg")
                .error(R.drawable.error_image)
                .into(holder.thumbnail)
        } catch (e: Exception) {
            Log.e("HomeAdapter", "이미지 로드 실패: $fileName", e)
        }
    }

    override fun getItemCount(): Int = webtoonList.size
}

