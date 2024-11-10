package com.example.webtoon_project.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.webtoon_project.R
import com.example.webtoon_project.databinding.FragmentHomeBinding
import com.example.webtoon_project.Retrofit.INodeJS
import com.example.webtoon_project.WebtoonAdapter
import com.example.webtoon_project.Retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter
    private lateinit var webtoonAdapter: WebtoonAdapter
    private lateinit var webtoonList: List<INodeJS.Webtoon> // 서버에서 받아올 웹툰 리스트

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        // RecyclerView 초기화
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        // webtoonList 초기화 (서버에서 데이터 가져오기)
        fetchWebtoonList()

        return binding.root
    }

    private fun fetchWebtoonList() {
        // 서버에서 웹툰 리스트 가져오기
        val retrofit = RetrofitClient.getInstance()
        val service = retrofit.create(INodeJS::class.java)

        service.getWebtoons().enqueue(object : Callback<List<INodeJS.Webtoon>> { //INodeJS 수정
            override fun onResponse(
                call: Call<List<INodeJS.Webtoon>>,
                response: Response<List<INodeJS.Webtoon>>
            ) {
                if (response.isSuccessful) {
                    webtoonList = response.body() ?: emptyList()
                    // 서버에서 데이터를 성공적으로 가져오면 어댑터 초기화 및 RecyclerView 업데이트
                    setupAdapter()
                } else {
                    Toast.makeText(context, "웹툰 목록 불러오기 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<INodeJS.Webtoon>>, t: Throwable) {
                Toast.makeText(context, "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupAdapter() {
        // 웹툰 어댑터 초기화 및 RecyclerView에 설정
        webtoonAdapter = WebtoonAdapter(webtoonList) { webtoonId ->
            // 웹툰 썸네일을 클릭했을 때 서버에 ID 전송
            sendWebtoonIdToServer(webtoonId)
        }
        recyclerView.adapter = webtoonAdapter
    }

    private fun sendWebtoonIdToServer(webtoonId: String) {
        // 서버로 ID 전송하고, 응답을 처리하는 코드
        val retrofit = RetrofitClient.getInstance()
        val service = retrofit.create(INodeJS::class.java)
        service.getRecommendedWebtoons(webtoonId).enqueue(object : Callback<List<INodeJS.Webtoon>> {
            override fun onResponse(
                call: Call<List<INodeJS.Webtoon>>,
                response: Response<List<INodeJS.Webtoon>>
            ) {
                if (response.isSuccessful) {
                    val recommendedWebtoons = response.body() ?: emptyList()
                    updateRecyclerView(recommendedWebtoons)
                } else {
                    Toast.makeText(context, "추천 웹툰 불러오기 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<INodeJS.Webtoon>>, t: Throwable) {
                Toast.makeText(context, "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateRecyclerView(webtoons: List<INodeJS.Webtoon>) {
        // 리사이클러뷰 업데이트
        val updatedAdapter = HomeAdapter(webtoons)
        recyclerView.adapter = updatedAdapter
    }
}

