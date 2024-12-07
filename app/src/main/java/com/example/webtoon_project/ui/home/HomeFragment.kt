package com.example.webtoon_project.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.webtoon_project.R
import com.example.webtoon_project.Retrofit.INodeJS
import com.example.webtoon_project.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var homeAdapter: HomeAdapter
    private var recommendedWebtoons: List<INodeJS.Webtoon> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // MainActivity에서 전달된 추천 웹툰 데이터를 arguments로 받음
        recommendedWebtoons = arguments?.getParcelableArrayList<INodeJS.Webtoon>("recommendedWebtoons") ?: emptyList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        recyclerView = binding.lstUser
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        // 어댑터 초기화 및 설정
        homeAdapter = HomeAdapter(recommendedWebtoons)
        recyclerView.adapter = homeAdapter

        return binding.root
    }

    private fun onWebtoonClick(webtoon: INodeJS.Webtoon) {
        // 클릭 로직
    }
}

