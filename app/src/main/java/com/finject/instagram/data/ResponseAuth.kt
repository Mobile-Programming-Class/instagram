package com.finject.instagram.data

import com.google.gson.annotations.SerializedName

//data class ResponseAuth()

// http://127.0.0.1:8000/api/user/login
data class ResponseLogin(

    @field:SerializedName("user")
    var user: User? = null,

    @field:SerializedName("access_token")
    var access_token: String? = null,

    @field:SerializedName("asset_link")
    var asset_link: String? = null
)

// http://127.0.0.1:8000/api/user/register
data class ResponseRegister(

    @field:SerializedName("user")
    var user: User? = null,

    @field:SerializedName("access_token")
    var access_token: String? = null,

    @field:SerializedName("asset_link")
    var asset_link: String? = null
)