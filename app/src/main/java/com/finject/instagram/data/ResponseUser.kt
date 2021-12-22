package com.finject.instagram.data

import com.google.gson.annotations.SerializedName

//data class ResponseUser()

// http://127.0.0.1:8000/api/user/profile
data class ResponseOwnProfile(

    @field:SerializedName("success")
    var success: Boolean? = null,

    @field:SerializedName("data")
    var data: User? = null,

    @field:SerializedName("message")
    var message: String? = null
)

// http://127.0.0.1:8000/api/user/profile/{id}
data class ResponseProfileById(

    @field:SerializedName("success")
    var success: Boolean? = null,

    @field:SerializedName("data")
    var data: User? = null,

    @field:SerializedName("message")
    var message: String? = null
)

// http://192.168.0.108:80/api/search-user/{keyword}
data class ResponseUserByKeyword(

    @field:SerializedName("success")
    var success: Boolean? = null,

    @field:SerializedName("data")
    var data: MutableList<User?> ?= null,

    @field:SerializedName("message")
    var message: String? = null
)
