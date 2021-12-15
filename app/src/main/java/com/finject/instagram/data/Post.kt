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
    var sentiment: Float? = null,

    @field:SerializedName("like_count")
    var like_count: Int? = null
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
"like_count": 1,
"user": {
    "id": 4,
    "name": "ei-chan",
    "email": "ei@gmail.com",
    "email_verified_at": null,
    "bio": "bio",
    "mobile": "082141414361",
    "city": "inazuma",
    "created_at": "2021-10-28T13:19:10.000000Z",
    "updated_at": "2021-12-08T10:45:32.000000Z",
    "avatar": "avatar-4.png"
},
"like": [
    {
        "id": 17,
        "id_user": 4,
        "id_post": 45,
        "created_at": "2021-12-14T03:34:49.000000Z",
        "updated_at": "2021-12-14T03:34:49.000000Z"
    }
]
 */