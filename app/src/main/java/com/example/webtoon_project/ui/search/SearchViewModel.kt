//package com.example.webtoon_project.ui.search
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.example.webtoon_project.Retrofit.INodeJS
//
//class SearchViewModel : ViewModel() {
//
//    private val _webtoons = MutableLiveData<List<INodeJS.Webtoon>>()
//    val webtoons: LiveData<List<INodeJS.Webtoon>> = _webtoons
//
//    private val fakeWebtoons = listOf(
//        INodeJS.Webtoon("The Empress's Revenge", ""),
//        INodeJS.Webtoon("The Villainous Queen", ""),
//        INodeJS.Webtoon("The Empress's Secret", ""),
//        INodeJS.Webtoon("The Path to Empress", ""),
//        INodeJS.Webtoon("General Webtoon Alpha", ""),
//        INodeJS.Webtoon("General Webtoon Beta", ""),
//        INodeJS.Webtoon("General Webtoon Gamma", ""),
//        INodeJS.Webtoon("General Webtoon Delta", ""),
//        INodeJS.Webtoon("The Queen's Betrayal", ""),
//        INodeJS.Webtoon("How to Rule as Queen", "")
//    )
//
//    fun searchWebtoons(query: String) {
//        val searchResults = fakeWebtoons.filter { it.title?.contains(query, ignoreCase = true) == true }
//            .take(10)
//        _webtoons.value = searchResults
//    }
//}