package com.example.webtoon_project.Retrofit

import android.os.Parcel
import android.os.Parcelable
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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

    @POST("api/recommend/get_webtoons")
    fun getWebtoonsByGenre(@Body genreRequest: GenreRequest?): Observable<List<Webtoon?>?>?

    @GET("api/webtoons/search")
    fun searchWebtoons(@Query("query") query: String): Observable<SearchResponse>

    @GET("api/webtoons")
    fun getWebtoons(): Call<List<Webtoon>>

    class GenreRequest(var genre: String)

    class Webtoon : Parcelable {
        var id: Int
        var title: String?
        var thumbnail_link: String?
        var synopsis: String?
        var similarity_score: Float?

        constructor(id: Int, title: String?, thumbnail_link: String?, synopsis: String?, similarity_score: Float?) {
            this.id = id
            this.title = title
            this.thumbnail_link = thumbnail_link
            this.synopsis = synopsis
            this.similarity_score = similarity_score
        }

        protected constructor(`in`: Parcel) {
            id = `in`.readInt()
            title = `in`.readString()
            thumbnail_link = `in`.readString()
            synopsis = `in`.readString()
            similarity_score = `in`.readFloat()
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(id)
            dest.writeString(title)
            dest.writeString(thumbnail_link)
            dest.writeString(synopsis)
            dest.writeFloat(similarity_score!!)
        }

        companion object CREATOR : Parcelable.Creator<Webtoon> {
            override fun createFromParcel(parcel: Parcel): Webtoon {
                return Webtoon(parcel)
            }

            override fun newArray(size: Int): Array<Webtoon?> {
                return arrayOfNulls(size)
            }
        }
    }

    // Flask 서버
    @POST("/recommend")
    @FormUrlEncoded
    fun sendClickDataToFlask(
        @Field("webtoon_id") webtoonId: String // 클릭 시 recommend 엔드포인트에 id를 String으로 보냄
    ): Observable<String>

    @GET("/recommend")
    fun getRecommendedWebtoons(@Query("id") webtoonId: String): Call<List<Webtoon>>

    // 기존 웹툰 리스트 받아오기
    data class Webtoon_2(
        val id: String, // id를 String으로 변경
        val title: String,
        val thumbnail_link: String
    )

    // 검색 결과와 추천 결과를 함께 받는 응답 클래스
    data class SearchResponse(
        val searchResult: Webtoon,
        val recommendations: List<Webtoon>
    )
}
