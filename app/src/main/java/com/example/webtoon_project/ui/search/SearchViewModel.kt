package com.example.webtoon_project.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.webtoon_project.Retrofit.INodeJS
import com.example.webtoon_project.Retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {

    private val _searchResults = MutableLiveData<INodeJS.SearchResponse>()
    val searchResults: LiveData<INodeJS.SearchResponse> = _searchResults

    private val _recommendations = MutableLiveData<List<INodeJS.Webtoon>>()
    val recommendations: LiveData<List<INodeJS.Webtoon>> = _recommendations

    private val _searchError = MutableLiveData<String>()
    val searchError: LiveData<String> = _searchError

    private val myAPI = RetrofitClient.getInstance().create(INodeJS::class.java)
    private val compositeDisposable = CompositeDisposable()

    fun searchWebtoons(query: String) {
        compositeDisposable.add(
            myAPI.searchWebtoons(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    _searchResults.value = response
                    _searchError.value = null
                }, { error ->
                    _searchError.value = error.message ?: "검색 중 오류가 발생했습니다."
                })
        )
    }

    fun getRecommendations(webtoonId: Int) {
        myAPI.getRecommendedWebtoons(webtoonId.toString()).enqueue(object : Callback<List<INodeJS.Webtoon>> {
            override fun onResponse(call: Call<List<INodeJS.Webtoon>>, response: Response<List<INodeJS.Webtoon>>) {
                if (response.isSuccessful) {
                    _recommendations.value = response.body()
                }
            }
            override fun onFailure(call: Call<List<INodeJS.Webtoon>>, t: Throwable) {
                // 에러 처리
            }
        })
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}