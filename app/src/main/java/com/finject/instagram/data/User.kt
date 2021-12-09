package com.finject.instagram.data

data class User(
    val id : Int?,
    var name : String? = null,
    var email : String? = null,
    var email_verified_at : String? = null,
    var bio : String? = null,
    var mobile : String? = null,
    var city : String? = null,
    var created_at : String? = null,
    var updated_at : String? = null,
    var avatar : String? = null,
)
/*
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

        "id": 13,
        "name": "zhong rex",
        "email": "zhongli@gmail.com",
        "email_verified_at": null,
        "bio": null,
        "mobile": null,
        "city": null,
        "created_at": "2021-12-05T20:04:10.000000Z",
        "updated_at": "2021-12-05T20:04:10.000000Z",
        "avatar": "avatar-13.png"
 */