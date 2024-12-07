package com.example.webtoon_project

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.webtoon_project.Retrofit.INodeJS

class WebtoonAdapter(private val webtoonList: List<INodeJS.Webtoon>,
                     private val onItemClick: (String) -> Unit // 클릭 이벤트 콜백 추가
) : RecyclerView.Adapter<WebtoonAdapter.WebtoonViewHolder>() {

    inner class WebtoonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail: ImageView = view.findViewById(R.id.webtoonThumbnail)
        val title: TextView = view.findViewById(R.id.webtoonTitle)

        init {
            // 클릭 리스너 추가
            view.setOnClickListener {
                val webtoon = webtoonList[adapterPosition]
                Log.d("WebtoonAdapter", "클릭된 웹툰 title: ${webtoon.title}")
                onItemClick(webtoon.title.toString())  // 클릭한 웹툰의 ID를 String으로 전달
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebtoonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reitem_webtoon, parent, false)
        return WebtoonViewHolder(view)
    }

    override fun onBindViewHolder(holder: WebtoonViewHolder, position: Int) {
        val webtoon = webtoonList[position]
        holder.title.text = webtoon.title

        // 웹툰 제목을 파일명 형식으로 변환
        val fileName = webtoon.title?.let { title ->
            title.replace(Regex("[\\\\/*?:\"<>|]"), "")
                .replace(" ", "_")
        }

        try {
            // Assets에서 이미지 로드
            Glide.with(holder.itemView.context)
                .load("file:///android_asset/thumbnails/${fileName}.jpg")
                .error(R.drawable.error_image)
                .into(holder.thumbnail)
        } catch (e: Exception) {
            Log.e("WebtoonAdapter", "이미지 로드 실패: $fileName", e)
        }
    }

    override fun getItemCount(): Int = webtoonList.size
}
