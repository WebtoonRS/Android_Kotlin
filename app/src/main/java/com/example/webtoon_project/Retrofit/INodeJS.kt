package com.example.webtoon_project.Retrofit

import android.os.Parcel
import android.os.Parcelable
import io.reactivex.Observable
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
    fun searchWebtoons(@Query("query") query: String): Observable<List<Webtoon>>

    class GenreRequest(var genre: String)

    class Webtoon : Parcelable {
        var id: Int // id 필드 추가함
        var title: String?
        var thumbnail_link: String?

        constructor(id: Int, title: String?, thumbnail_link: String?) {
            this.id = id
            this.title = title
            this.thumbnail_link = thumbnail_link
        }

        protected constructor(`in`: Parcel) {
            id = `in`.readInt() // id 읽기 추가
            title = `in`.readString()
            thumbnail_link = `in`.readString()
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(id) // id 쓰기 추가
            dest.writeString(title)
            dest.writeString(thumbnail_link)
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

    //flask 서버
    @POST("recommend")
    @FormUrlEncoded
    fun sendClickDataToFlask(
        @Field("webtoon_id") webtoonId: String // 클릭 시 recommend 엔드포인트에 id를 보냄
    ): Observable<String>
}
