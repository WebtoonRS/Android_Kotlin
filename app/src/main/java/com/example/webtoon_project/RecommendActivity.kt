package com.example.webtoon_project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.webtoon_project.databinding.ActivityRecommendBinding
import com.example.webtoon_project.Retrofit.INodeJS
import com.example.webtoon_project.Retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RecommendActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecommendBinding
    private lateinit var myAPI: INodeJS
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrofit API 초기화
        myAPI = RetrofitClient.getInstance().create(INodeJS::class.java)

        // RecyclerView 설정
        binding.webtoonRecyclerView.layoutManager = GridLayoutManager(this, 3  )

        // 장르 버튼 리스너 설정
        setGenreButtonListener(R.id.sectionramance, "로맨스")
        setGenreButtonListener(R.id.sectionfantasy, "판타지")
        setGenreButtonListener(R.id.sectionThriller, "스릴러")
        setGenreButtonListener(R.id.sectionDrama, "드라마")
        setGenreButtonListener(R.id.sectionMA, "무협/사극")
        setGenreButtonListener(R.id.sectionComic, "일상/개그")
        setGenreButtonListener(R.id.sectionSports, "스포츠/액션")

        // BottomNavigationView의 Next 버튼 설정
        binding.navNext.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_next -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }


    private fun setGenreButtonListener(buttonId: Int, genre: String) {
        findViewById<Button>(buttonId).setOnClickListener {
            Toast.makeText(this, "$genre 장르가 선택되었습니다.", Toast.LENGTH_SHORT).show()
            fetchWebtoonsByGenre(genre)
        }
    }

    private fun fetchWebtoonsByGenre(genre: String) {
        val genreRequest = INodeJS.GenreRequest(genre)
        compositeDisposable.add(
            myAPI.getWebtoonsByGenre(genreRequest)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ webtoonList ->
                    val validWebtoons = webtoonList?.filterNotNull()?.map { webtoon ->
                        WebtoonItem(
                            id = webtoon.id,
                            title = webtoon.title ?: "",
                            author = webtoon.writer?.replace(" / ", ", ") ?: "작가 미상",
                            thumbnailUrl = webtoon.thumbnail_link ?: "",
                            synopsis = webtoon.synopsis ?: ""
                        )
                    } ?: emptyList()
                    
                    binding.webtoonRecyclerView.adapter = WebtoonAdapter(
                        webtoons = validWebtoons,
                        onItemClick = { webtoonId -> sendWebtoonIdToServer(webtoonId.toString()) }
                    )
                }, { error ->
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                })
                ?: return
        )
    }

    private fun sendWebtoonIdToServer(webtoonId: String) {
        Toast.makeText(this, "웹툰 ID: $webtoonId 클릭됨", Toast.LENGTH_SHORT).show()
        // 여기에 실제 서버 요청 코드를 추가하면 됩니다.
    }


    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
