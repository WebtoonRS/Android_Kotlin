package com.example.webtoon_project.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.webtoon_project.Retrofit.INodeJS
import com.example.webtoon_project.Retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException

class SearchViewModel : ViewModel() {

    private val _searchResults = MutableLiveData<INodeJS.SearchResponse>()
    val searchResults: LiveData<INodeJS.SearchResponse> = _searchResults

    private val _recommendResults = MutableLiveData<List<INodeJS.Webtoon>>()
    val recommendResults: LiveData<List<INodeJS.Webtoon>> = _recommendResults

    private val _searchError = MutableLiveData<String>()
    val searchError: LiveData<String> = _searchError

    private val _searchMode = MutableLiveData<SearchMode>()
    val searchMode: LiveData<SearchMode> = _searchMode

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _selectedWebtoon = MutableLiveData<INodeJS.Webtoon>()
    val selectedWebtoon: LiveData<INodeJS.Webtoon> = _selectedWebtoon

    private val searchAPI = RetrofitClient.getInstance().create(INodeJS::class.java)
    private val recommendAPI = RetrofitClient.getInstance5000().create(INodeJS::class.java)
    private val compositeDisposable = CompositeDisposable()

    enum class SearchMode {
        STORY, STYLE
    }

    fun searchWebtoons(query: String, mode: SearchMode) {
        _searchMode.value = mode
        _isLoading.value = true

        compositeDisposable.add(
            searchAPI.searchWebtoons(query, mode.toString().lowercase())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    _searchResults.value = response
                    _searchError.value = null
                    _isLoading.value = false
                }, { error ->
                    handleError(error)
                })
        )
    }

    fun getRecommendations(webtoon: INodeJS.Webtoon) {
        _isLoading.value = true
        val mode = _searchMode.value ?: SearchMode.STORY
        println("추천 요청 - 제목: ${webtoon.title}, 모드: $mode")

        val request = INodeJS.RecommendRequest(
            title = webtoon.title ?: ""
        )

        val apiCall = when (mode) {
            SearchMode.STORY -> recommendAPI.getStoryRecommendations(request)
            SearchMode.STYLE -> recommendAPI.getStyleRecommendations(request)
        }

        compositeDisposable.add(
            apiCall
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ recommendations ->
                    println("추천 결과 수신: ${recommendations.size}개")
                    _recommendResults.value = recommendations
                    _isLoading.value = false
                }, { error ->
                    println("추천 API 에러: ${error.message}")
                    handleError(error)
                    _isLoading.value = false
                })
        )
    }

    private fun handleError(error: Throwable) {
        val errorMessage = when {
            error is HttpException && error.code() == 404 ->
                "추천 웹툰을 찾을 수 없습니다."
            error is HttpException && error.code() == 500 ->
                "서버 오류가 발생했습니다."
            error is IOException ->
                "네트워크 연결을 확인해주세요."
            else ->
                "오류가 발생했습니다: ${error.localizedMessage}"
        }
        _searchError.value = errorMessage
        _isLoading.value = false
        println("에러 발생: $errorMessage")
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
