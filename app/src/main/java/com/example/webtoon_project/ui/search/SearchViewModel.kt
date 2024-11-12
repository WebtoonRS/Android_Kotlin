package com.example.webtoon_project.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.webtoon_project.Retrofit.INodeJS
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchViewModel : ViewModel() {

    private val _searchResults = MutableLiveData<INodeJS.SearchResponse>()
    val searchResults: LiveData<INodeJS.SearchResponse> = _searchResults

    private val _recommendations = MutableLiveData<List<INodeJS.Webtoon>>()
    val recommendations: LiveData<List<INodeJS.Webtoon>> = _recommendations

    private val myAPI = RetrofitClient.getInstance().create(INodeJS::class.java)
    private val compositeDisposable = CompositeDisposable()

    fun searchWebtoons(query: String) {
        compositeDisposable.add(
            myAPI.searchWebtoons(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    _searchResults.value = response
                }, { error ->
                    // 에러 처리
                })
        )
    }

    fun getRecommendations(webtoonId: Int) {
        compositeDisposable.add(
            myAPI.getRecommendedWebtoons(webtoonId.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ recommendations ->
                    _recommendations.value = recommendations
                }, { error ->
                    // 에러 처리
                })
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}