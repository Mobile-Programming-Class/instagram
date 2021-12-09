package com.finject.instagram.data

import com.google.gson.annotations.SerializedName

data class Comment(

    @field:SerializedName("success")
    var success: Boolean? = null,

    @field:SerializedName("data")
    var data: MutableList<ItemComment?>? = null,

    @field:SerializedName("message")
    var message: String? = null
)

data class ItemComment(

    @field:SerializedName("success")
    var id : Int? = null,

    @field:SerializedName("isi")
    var isi : String? = null,

    @field:SerializedName("id_post")
    var id_post : Int? = null,

    @field:SerializedName("id_user")
    var id_user : Int? = null,

    @field:SerializedName("created_at")
    var created_at : String? = null,

    @field:SerializedName("updated_at")
    var updated_at : String? = null,

    @field:SerializedName("sentiment")
    var sentiment : Float? = null,

    @field:SerializedName("user")
    var user : User? = null
)

/*
"data": [
        {
            "id": 7,
            "isi": "wew",
            "id_post": 44,
            "id_user": 4,
            "created_at": "2021-12-07T06:07:14.000000Z",
            "updated_at": "2021-12-07T06:07:14.000000Z",
            "sentiment": null,
            "user": {
                "id": 4,
                "name": "ei",
                "email": "ei@gmail.com",
                "email_verified_at": null,
                "bio": "bio",
                "mobile": "082141414361",
                "city": "inazuma",
                "created_at": "2021-10-28T13:19:10.000000Z",
                "updated_at": "2021-12-05T19:42:00.000000Z",
                "avatar": "avatar-4.png"
            }
        },
        ]
 */