package com.example.webtoon_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.webtoon_project.Retrofit.INodeJS
import com.example.webtoon_project.Retrofit.RetrofitClient
import com.example.webtoon_project.databinding.ActivityRecommendBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecommendBinding
    private lateinit var myAPI: INodeJS
    private lateinit var myAPI5000: INodeJS // 5000 포트용 API
    private val compositeDisposable = CompositeDisposable()
    private val selectedTitles = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrofit API 초기화
        myAPI = RetrofitClient.getInstance().create(INodeJS::class.java)
        // Retrofit API 초기화 (5000 포트 사용)
        myAPI5000 = RetrofitClient.getInstance5000().create(INodeJS::class.java)

        // RecyclerView 설정
        binding.webtoonRecyclerView.layoutManager = GridLayoutManager(this, 3)

        // 장르 버튼 리스너 설정
        setGenreButtonListener(R.id.sectionramance, "로맨스")
        setGenreButtonListener(R.id.sectionfantasy, "판타지")
        setGenreButtonListener(R.id.sectionThriller, "스릴러")
        setGenreButtonListener(R.id.sectionDrama, "드라마")
        setGenreButtonListener(R.id.sectionMA, "무협/사극")
        setGenreButtonListener(R.id.sectionComic, "일상/개그")
        setGenreButtonListener(R.id.sectionSports, "스포츠/액션")


        // Next 버튼 클릭 리스너
        binding.navNext.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_next -> {
                    sendSelectedTitlesToServer()
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
                    val validWebtoons = webtoonList?.filterNotNull() ?: emptyList()
                    binding.webtoonRecyclerView.adapter = WebtoonAdapter(validWebtoons) { webtoonTitle ->
                        // 웹툰 클릭 시 Flask 서버로 title 전송 후 추천 웹툰 목록 받기
                        addWebtoonTitle(webtoonTitle)
                    }
                }, { error ->
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                })
                ?: return
        )
    }

    private fun addWebtoonTitle(webtoonTitle: String) {
        if (!selectedTitles.contains(webtoonTitle)) {
            selectedTitles.add(webtoonTitle)
            Toast.makeText(this, "$webtoonTitle 이 선택되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "$webtoonTitle 은 이미 선택되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendSelectedTitlesToServer() {
        if (selectedTitles.isEmpty()) {
            Toast.makeText(this, "선택된 제목이 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val requestBody = INodeJS.WebtoonTitleRequest(selectedTitles)

        myAPI5000.sendClickDataToFlask(requestBody).enqueue(object : Callback<List<INodeJS.Webtoon>> {
            override fun onResponse(call: Call<List<INodeJS.Webtoon>>, response: Response<List<INodeJS.Webtoon>>) {
                if (response.isSuccessful) {
                    val recommendedWebtoons = response.body() ?: emptyList()
                    Log.d("RecommendActivity", "추천 웹툰 목록: $recommendedWebtoons")

                    // MainActivity로 추천 웹툰 전달
                    val intent = Intent(this@RecommendActivity, MainActivity::class.java)
                    intent.putParcelableArrayListExtra("recommendedWebtoons", ArrayList(recommendedWebtoons))
                    startActivity(intent)
                } else {
                    Log.e("RecommendActivity", "추천 요청 실패: ${response.code()}")
                    Toast.makeText(this@RecommendActivity, "추천 요청 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<INodeJS.Webtoon>>, t: Throwable) {
                Log.e("RecommendActivity", "서버 연결 실패: ${t.message}")
                Toast.makeText(this@RecommendActivity, "서버 연결 실패: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

//    private fun fetchRecommendedWebtoons() {
//        val requestBody = INodeJS.WebtoonTitleRequest(selectedTitles)
//
//        myAPI5000.getRecommendedWebtoons(requestBody).enqueue(object : Callback<INodeJS.WebtoonResponse> {
//            override fun onResponse(
//                call: Call<INodeJS.WebtoonResponse>,
//                response: Response<INodeJS.WebtoonResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val recommendedWebtoons = response.body()?.recommendations ?: emptyList()
//                    val intent = Intent(this@RecommendActivity, MainActivity::class.java)
//                    intent.putParcelableArrayListExtra("recommendedWebtoons", ArrayList(recommendedWebtoons))
//                    startActivity(intent)
//                } else {
//                    Log.e("RecommendActivity", "추천 웹툰 불러오기 실패: ${response.code()}")
//                    Toast.makeText(this@RecommendActivity, "추천 웹툰 불러오기 실패", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<INodeJS.WebtoonResponse>, t: Throwable) {
//                Log.e("RecommendActivity", "서버 연결 실패: ${t.message}")
//                Toast.makeText(this@RecommendActivity, "서버 연결 실패", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }




    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
