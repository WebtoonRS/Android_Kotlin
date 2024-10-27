package com.example.webtoon_project.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.webtoon_project.Retrofit.INodeJS
import com.example.webtoon_project.Retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DashboardViewModel : ViewModel() {

    private val _webtoons = MutableLiveData<List<INodeJS.Webtoon>>()
    val webtoons: LiveData<List<INodeJS.Webtoon>> = _webtoons

    private val compositeDisposable = CompositeDisposable()
    private val myAPI: INodeJS = RetrofitClient.instance?.create(INodeJS::class.java)
        ?: throw IllegalStateException("API service not initialized")

    fun searchWebtoons(query: String) {
        compositeDisposable.add(myAPI.searchWebtoons(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ webtoonList ->
                _webtoons.value = webtoonList
            }, { throwable ->
                // 에러 처리
                println("Error: ${throwable.message}")
            })
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
