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

class WebtoonAdapter(
    private var webtoons: List<INodeJS.Webtoon>,
    private val onItemClick: (Int) -> Unit,
    private val onSynopsisClick: ((Int) -> Unit)? = null
) : RecyclerView.Adapter<WebtoonAdapter.WebtoonViewHolder>() {

    fun updateList(newWebtoons: List<INodeJS.Webtoon>) {
        webtoons = newWebtoons
        notifyDataSetChanged()
    }

    inner class WebtoonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail: ImageView = view.findViewById(R.id.webtoonThumbnail)
        val title: TextView = view.findViewById(R.id.webtoonTitle)
        val synopsis: TextView? = view.findViewById(R.id.webtoonSynopsis)

        init {
            synopsis?.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onSynopsisClick?.invoke(webtoons[position].id)
                }
            }

            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(webtoons[position].id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebtoonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reitem_webtoon, parent, false)
        return WebtoonViewHolder(view)
    }

    override fun onBindViewHolder(holder: WebtoonViewHolder, position: Int) {
        val webtoon = webtoons[position]
        holder.title.text = webtoon.title
        holder.synopsis?.text = webtoon.synopsis

        val url = GlideUrl(webtoon.thumbnail_link, LazyHeaders.Builder()
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
            .build()
        )

        Glide.with(holder.itemView.context)
            .load(url)
            .error(R.drawable.error_image)
            .into(holder.thumbnail)
    }

    override fun getItemCount(): Int = webtoons.size
}
