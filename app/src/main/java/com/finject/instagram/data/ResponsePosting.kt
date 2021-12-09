package com.finject.instagram.data

import com.google.gson.annotations.SerializedName

// http://127.0.0.1:8000/api/posts
data class ResponsePostingGetAll(

    @field:SerializedName("success")
    var success: Boolean? = null,

    @field:SerializedName("data")
    var data: MutableList<PostGet?>? = null,

    @field:SerializedName("message")
    var message: String? = null
)

// http://127.0.0.1:8000/api/posts/44
data class ResponsePostingGetById(

    @field:SerializedName("success")
    var success: Boolean? = null,

    @field:SerializedName("data")
    var data: Post? = null,

    @field:SerializedName("message")
    var message: String? = null
)

// See General
//http://127.0.0.1:8000/api/user/posting
//http://127.0.0.1:8000/api/user/posting/45
//http://127.0.0.1:8000/api/user/posting/delete/43
//http://127.0.0.1:8000/api/user/liking/44
