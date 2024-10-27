package com.example.webtoon_project

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.webtoon_project.databinding.ActivityRecommendBinding

class RecommendActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityRecommendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fun setGenreButtonListener(buttonId: Int, genre: String) {
            val button = findViewById<Button>(buttonId)
            button.setOnClickListener { v: View? ->
                Toast.makeText(this, "$genre 장르가 선택되었습니다.", Toast.LENGTH_SHORT).show()
                // 여기에 추가적인 처리 로직을 넣을 수 있습니다.
            }
        }

        setGenreButtonListener(R.id.sectionramance, "로맨스")
        setGenreButtonListener(R.id.sectionfantasy, "판타지")
        setGenreButtonListener(R.id.sectionThriller, "스릴러")
        setGenreButtonListener(R.id.sectionDrama, "드라마")
        setGenreButtonListener(R.id.sectionMA, "무협/사극")
        setGenreButtonListener(R.id.sectionComic, "일상/개그")
        setGenreButtonListener(R.id.sectionSports, "스포츠/액션")

    }




}