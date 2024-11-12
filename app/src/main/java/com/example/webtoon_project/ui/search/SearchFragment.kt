package com.example.webtoon_project.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.webtoon_project.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

   private var _binding: FragmentSearchBinding? = null
   private val binding get() = _binding!!
   private lateinit var viewModel: SearchViewModel
   private lateinit var searchAdapter: WebtoonAdapter
   private lateinit var recommendAdapter: WebtoonAdapter

   override fun onCreateView(
       inflater: LayoutInflater,
       container: ViewGroup?,
       savedInstanceState: Bundle?
   ): View {
       _binding = FragmentSearchBinding.inflate(inflater, container, false)
       viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
       return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       super.onViewCreated(view, savedInstanceState)

       setupRecyclerViews()
       setupSearchView()
       observeViewModel()
   }

   private fun setupRecyclerViews() {
       // 검색 결과를 위한 RecyclerView
       binding.searchResultRecyclerView.apply {
           layoutManager = LinearLayoutManager(context)
           searchAdapter = WebtoonAdapter(
               emptyList(),
               onItemClick = { webtoonId ->
                   // 일반 클릭 처리
               },
               onSynopsisClick = { webtoonId ->
                   // 줄거리 클릭 시 추천 요청
                   viewModel.getRecommendations(webtoonId)
               }
           )
           adapter = searchAdapter
       }

       // 추천 결과를 위한 RecyclerView
       binding.recommendationsRecyclerView.apply {
           layoutManager = GridLayoutManager(context, 3)
           recommendAdapter = WebtoonAdapter(
               emptyList(),
               onItemClick = { webtoonId ->
                   // 추천 웹툰 클릭 처리
               }
           )
           adapter = recommendAdapter
       }
   }

   private fun setupSearchView() {
       binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
           override fun onQueryTextSubmit(query: String?): Boolean {
               query?.let { viewModel.searchWebtoons(it) }
               return true
           }

           override fun onQueryTextChange(newText: String?): Boolean = false
       })
   }

   private fun observeViewModel() {
       // 검색 결과 관찰
       viewModel.searchResults.observe(viewLifecycleOwner) { response ->
           searchAdapter.updateList(listOf(response.searchResult))
       }

       // 추천 결과 관찰
       viewModel.recommendations.observe(viewLifecycleOwner) { recommendations ->
           recommendAdapter.updateList(recommendations)
           binding.recommendationsSection.visibility = View.VISIBLE
       }
   }

   override fun onDestroyView() {
       super.onDestroyView()
       _binding = null
   }
}