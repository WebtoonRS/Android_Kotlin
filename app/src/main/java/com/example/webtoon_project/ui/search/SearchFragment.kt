package com.example.webtoon_project.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.webtoon_project.WebtoonAdapter
import com.example.webtoon_project.WebtoonItem
import com.example.webtoon_project.databinding.FragmentSearchBinding
import android.content.Context

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
       viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
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
               emptyList()
           ) { webtoonId ->
               // 웹툰 클릭 시 처리
               viewModel.getRecommendations(webtoonId)
           }
           adapter = searchAdapter
       }

       // 추천 결과를 위한 RecyclerView
       binding.recommendationsRecyclerView.apply {
           layoutManager = GridLayoutManager(context, 3)
           recommendAdapter = WebtoonAdapter(
               emptyList()
           ) { webtoonId ->
               // 추천 웹툰 클릭 시 처리
           }
           adapter = recommendAdapter
       }
   }

   private fun setupSearchView() {
       binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
           override fun onQueryTextSubmit(query: String?): Boolean {
               query?.let { 
                   viewModel.searchWebtoons(it)
                   // 키보드 숨기기
                   val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                   imm.hideSoftInputFromWindow(view?.windowToken, 0)
               }
               return true
           }

           override fun onQueryTextChange(newText: String?): Boolean {
               // 실시간 검색 구현 시 여기에 추가
               return false
           }
       })
   }

   private fun observeViewModel() {
       viewModel.searchResults.observe(viewLifecycleOwner) { response ->
           if (response != null) {
               val webtoonItems = listOf(response.searchResult).map { webtoon ->
                   WebtoonItem(
                       id = webtoon.id,
                       title = webtoon.title ?: "",
                       thumbnailUrl = webtoon.thumbnail_link ?: "",
                       synopsis = webtoon.synopsis ?: "",
                       author = webtoon.writer ?: ""
                   )
               }
               searchAdapter.updateWebtoons(webtoonItems)
           }
       }

       viewModel.searchError.observe(viewLifecycleOwner) { error ->
           error?.let {
               Toast.makeText(context, "검색 오류: $it", Toast.LENGTH_SHORT).show()
           }
       }

       viewModel.recommendations.observe(viewLifecycleOwner) { recommendations ->
           if (!recommendations.isNullOrEmpty()) {
               val webtoonItems = recommendations.map { webtoon ->
                   WebtoonItem(
                       id = webtoon.id,
                       title = webtoon.title ?: "",
                       thumbnailUrl = webtoon.thumbnail_link ?: "",
                       synopsis = webtoon.synopsis ?: "",
                       author = webtoon.writer ?: ""
                   )
               }
               recommendAdapter.updateWebtoons(webtoonItems)
               binding.recommendationsSection.visibility = View.VISIBLE
           }
       }
   }

   override fun onDestroyView() {
       super.onDestroyView()
       _binding = null
   }
}