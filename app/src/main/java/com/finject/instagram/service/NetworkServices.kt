package com.finject.instagram.service

import com.finject.instagram.data.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

private const val BASE_URL = "http://192.168.0.108:80/api/"

interface NetworkServices {

    // TODO: header token
    // http://127.0.0.1:8000/api/user/login
    @FormUrlEncoded
    @POST("user/login")
    fun login(
        @Field("email") email: String?,
        @Field("password") password: String?,
    ) : Call<ResponseLogin>

    // http://127.0.0.1:8000/api/user/register
    @FormUrlEncoded
    @POST("user/register")
    fun register(
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("name") name: String?,
        @Field("bio") bio: String?,
        @Field("mobile") mobile: String?,
        @Field("city") city: String?,
        @Field("avatar") avatar: String?,
    ) : Call<ResponseRegister>

    // http://127.0.0.1:8000/api/user/profile
    @GET("user/profile")
    fun getProfile(@Header("Authorization") token: String): Call<ResponseOwnProfile>

    // http://127.0.0.1:8000/api/user/profile/{id}
    @GET("user/profile/{id}")
    fun getProfileById(): Call<ResponseProfileById>

    // http://127.0.0.1:8000/api/posts
    @GET("posts")
    fun getAllPosts(): Call<ResponsePostingGetAll>

    // http://127.0.0.1:8000/api/posts/44
    @GET("posts/{id}")
    fun getPostById(@Path("id") id: String): Call<ResponsePostingGetById>

    //http://127.0.0.1:8000/api/user/posting
    @FormUrlEncoded
    @POST("user/posting")
    fun posting(
        @Header("Authorization") token: String,
        @Field("caption") caption: String?,
        @Field("foto") foto: String?,
        @Field("sentiment") sentiment: Float?
    ) : Call<General>

    //http://127.0.0.1:8000/api/user/posting/45
    @FormUrlEncoded
    @POST("user/posting/{id}")
    fun editPosting(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Field("caption") caption: String?,
        @Field("foto") foto: String?,
        @Field("sentiment") sentiment: Float?
    ) : Call<General>

    //http://127.0.0.1:8000/api/user/posting/delete/43
    @GET("user/posts/delete/{id}")
    fun deletePosting(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<General>

    //http://127.0.0.1:8000/api/user/liking/44
    @GET("user/liking/{id}")
    fun liking(
        @Header("Authorization") token: String?,
        @Path("id") id: String?
    ): Call<General>

    @GET("user/favourites")
    fun getFavouritesPosts(
        @Header("Authorization") token: String?
    ): Call<ResponsePostingGetAll>

    // http://127.0.0.1:8000/api/comment/by-post-id/44
    @GET("comment/by-post-id/{id}")
    fun getCommentByPostId(
        @Path("id") id: String
    ): Call<Comment>

    // http://127.0.0.1:8000/api/comment/by-user-id/13
    @GET("comment/by-user-id/{id}")
    fun getCommentByUserId(@Path("id") id: String): Call<Comment>

    // http://127.0.0.1:8000/api/user/comment/create
    @FormUrlEncoded
    @POST("user/comment/create")
    fun createComment(
        @Header("Authorization") token: String,
        @Field("sentiment") sentiment: String?,
        @Field("isi") isi: String?,
        @Field("id_post") id_post: Int?
    ) : Call<ResponseComment>

    // http://127.0.0.1:8000/api/user/comment/edit/10
    @FormUrlEncoded
    @POST("user/comment/edit/{id}")
    fun editComment(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Field("sentiment") sentiment: String?,
        @Field("isi") isi: String?,
        @Field("id_post") id_post: Int?
    ) : Call<ResponseComment>

    // http://127.0.0.1:8000/api/user/comment/delete/{id}
    @GET("user/comment/delete/{id}")
    fun deleteComment(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<General>

    @GET("followers/{id}")
    fun getFollowersById(
        @Path("id") id: String
    ): Call<Follower>

    // http://192.168.0.108:80/api/search-user/{keyword}
    @GET("search-user/{keyword}")
    fun getUserByKeyword(
        @Path("keyword") keyword: String
    ): Call<ResponseUserByKeyword>

    @GET("user/chat/all")
    fun getChatGroup(
        @Header("Authorization") token: String?,
    ): Call<ResponseGroupChat>

    @GET("user/chat/{id}")
    fun getChats(
        @Header("Authorization") token: String?,
        @Path("id") id: String?
    ): Call<ResponseChat>

    @FormUrlEncoded
    @POST("user/chat/create-group")
    fun createChatGroup(
        @Header("Authorization") token: String?,
        @Field("members") members: String?,
    ): Call<ResponseGroupChat>

    @FormUrlEncoded
    @POST("user/chat/send-chat")
    fun sendChat(
        @Header("Authorization") token: String?,
        @Field("content") content: String?,
        @Field("id_chat") id_chat: String,
        @Field("sentiment") sentiment: String,
    ): Call<ResponseSendChat>
    /*
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
    */

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