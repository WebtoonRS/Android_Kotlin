package com.example.webtoon_project.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.webtoon_project.R
import com.example.webtoon_project.Retrofit.INodeJS

class HomeAdapter(private val webtoonList: List<INodeJS.Webtoon>) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail: ImageView = view.findViewById(R.id.webtoonThumbnail)

        init {
            view.setOnClickListener {
                // 클릭 처리 코드
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reitem_webtoon, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val webtoon = webtoonList[position]
        // 썸네일 이미지를 로드하는 코드
    }

    override fun getItemCount(): Int = webtoonList.size
}

