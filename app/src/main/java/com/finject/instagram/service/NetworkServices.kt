package com.finject.instagram.service

import com.finject.instagram.data.Post
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

private const val BASE_URL = "http://192.168.0.103:80/api/"

interface NetworkServices {
    @GET("foto")
    fun getAll(): Call<Post>

    @FormUrlEncoded
    @POST("upfoto")
    fun uploadFoto(
        @Field("caption") action: String?,
        @Field("foto") query: String?,
    ) : Call<General>

//    @GET("data/2.5/onecall?lat=-8.0983&lon=112.1681&appid=4225c726885f6edf5245c653a43b7597")
//    fun getWeather(): Call<Weather>


    @GET("foto/{id}")
    fun deleteById(@Path("id") id: String): Call<General>
}

object DataServices {
    fun create(): NetworkServices {
        val retrofit = Retrofit.Builder()
            // convert json to kotlin object
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
        return retrofit.create(NetworkServices::class.java)
    }
}