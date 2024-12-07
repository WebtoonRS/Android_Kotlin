package com.example.webtoon_project.Retrofit

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import kotlinx.parcelize.Parcelize
import retrofit2.Call
import retrofit2.http.*

interface INodeJS {
    @POST("api/auth/register")
    @FormUrlEncoded
    fun registerUser(
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("name") name: String?
    ): Observable<String?>?

    @POST("api/auth/login")
    @FormUrlEncoded
    fun loginUser(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Observable<String?>?

    @POST("api/random/get_webtoons")
    fun getWebtoonsByGenre(@Body genreRequest: GenreRequest?): Observable<List<Webtoon?>?>?

    @GET("api/webtoons/search")
    fun searchWebtoons(
        @Query("query") query: String,
        @Query("type") type: String
    ): Observable<SearchResponse>

    // 스타일 추천 메서드 추가
    @POST("api/webtoons/style_recommendations")
    fun getStyleRecommendations(
        @Body recommendRequest: RecommendRequest
    ): Observable<List<Webtoon>>

    // 스토리 추천 메서드 추가
    @POST("api/webtoons/story_recommendations")
    fun getStoryRecommendations(
        @Body recommendRequest: RecommendRequest
    ): Observable<List<Webtoon>>

    @POST("/recommend")
    @FormUrlEncoded
    fun sendClickDataToFlask(
        @Field("webtoon_id") webtoonId: String
    ): Observable<String>

    @GET("/recommend")
    fun getRecommendedWebtoons(@Query("id") webtoonId: String): Call<List<Webtoon>>

    @GET("api/webtoons")
    fun getWebtoons(): Call<List<Webtoon>>

    @POST("/api/webtoons/multiple_story_recommendations")
    fun sendClickDataToFlask(
        @Body requestBody: WebtoonTitleRequest
    ): Call<List<Webtoon>>


    data class WebtoonTitleRequest(
        val titles: List<String> // 서버로 보낼 웹툰 title을 담는 데이터 클래스
    )

    data class WebtoonResponse(
        val recommendations: List<Webtoon>
    )

    @POST("/api/webtoons/multiple_story_recommendations")
    fun getRecommendedWebtoons(
        @Body requestBody: WebtoonTitleRequest
    ): Call<WebtoonResponse>


    data class GenreRequest(var genre: String)

    data class SearchResponse(
        @SerializedName("searchResults") val searchResults: List<Webtoon>,
        @SerializedName("totalCount") val totalCount: Int
    )

    data class RecommendRequest(
        @SerializedName("title") val title: String
    )

    @Parcelize
    data class Webtoon(
        val id: Int,
        val title: String?,
        val author: String?,
        val synopsis: String?,
        @SerializedName("Thumb") val thumbnailLink: String?,
        @SerializedName("similarity_score") val similarityScore: Float?
    ) : Parcelable
}
