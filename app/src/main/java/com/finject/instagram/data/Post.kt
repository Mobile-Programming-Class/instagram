package com.finject.instagram.data

import com.google.gson.annotations.SerializedName

data class Post(

    @field:SerializedName("id")
    var id: Int,

    @field:SerializedName("foto")
    var foto: String? = null,

    @field:SerializedName("caption")
    var caption: String? = null,

    @field:SerializedName("id_user")
    var id_user: Int? = null,

    @field:SerializedName("created_at")
    var created_at: String? = null,

    @field:SerializedName("updated_at")
    var updated_at: String? = null,

    @field:SerializedName("sentiment")
    var sentiment: Float? = null
)

data class PostGet(

    @field:SerializedName("id")
    var id: Int?,

    @field:SerializedName("foto")
    var foto: String? = null,

    @field:SerializedName("caption")
    var caption: String? = null,

    @field:SerializedName("id_user")
    var id_user: Int? = null,

    @field:SerializedName("created_at")
    var created_at: String? = null,

    @field:SerializedName("updated_at")
    var updated_at: String? = null,

    @field:SerializedName("sentiment")
    var sentiment: Float? = null,

    @field:SerializedName("user")
    var user: User? = null,

    @field:SerializedName("like_count")
    var like_count: Int? = null
)
/*
"id": 44,
"foto": "foto-44.jpeg",
"caption": "urlencoded",
"id_user": 4,
"created_at": "2021-12-06T20:21:54.000000Z",
"updated_at": "2021-12-06T20:21:54.000000Z",
"sentiment": 0.7
 */