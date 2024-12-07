// package com.example.webtoon_project.ui.home

// import android.view.LayoutInflater
// import android.view.View
// import android.view.ViewGroup
// import android.widget.ImageView
// import androidx.recyclerview.widget.RecyclerView
// import com.example.webtoon_project.R

// class SliderAdapter(private val images: List<Int>) : RecyclerView.Adapter<SliderAdapter.ViewHolder>() {

//     inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//         val imageView: ImageView = view.findViewById(R.id.webtoonRecommend)
//     }

//     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//         val view = LayoutInflater.from(parent.context)
//             .inflate(R.layout.item_home, parent, false)
//         return ViewHolder(view)
//     }

//     override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//         holder.imageView.setImageResource(images[position])
//     }

//     override fun getItemCount(): Int = images.size
// }