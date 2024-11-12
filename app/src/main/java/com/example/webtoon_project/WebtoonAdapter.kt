package com.example.webtoon_project

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
                onItemClick(webtoon.id.toString())  // 클릭한 웹툰의 ID를 String으로 전달
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

        // User-Agent를 설정하여 Glide에 URL 로딩 !! url 가져오려면 설정 필수
        val url = GlideUrl(webtoon.thumbnail_link, LazyHeaders.Builder()
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
            .build()
        )

        // Glide를 사용하여 이미지 로딩
        Glide.with(holder.itemView.context)
            .load(url) // 이미지 로딩 전 표시할 플레이스홀더
            .error(R.drawable.error_image)  // 로딩 실패 시 표시할 이미지
            .into(holder.thumbnail)
    }

    override fun getItemCount(): Int = webtoonList.size
}
