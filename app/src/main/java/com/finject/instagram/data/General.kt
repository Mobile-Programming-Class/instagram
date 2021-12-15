package com.finject.instagram.data

import com.google.gson.annotations.SerializedName

data class General(

    @field:SerializedName("success")
    var success: Boolean? = null,

    @field:SerializedName("data")
    var data: String? = null,

    @field:SerializedName("message")
    var message: String? = null
)

data class Follower(
    @field:SerializedName("success")
    var success: Boolean? = null,

    @field:SerializedName("data")
    var data: Count? = null,

    @field:SerializedName("message")
    var message: String? = null
)

data class Count(
    @field:SerializedName("followers_count")
    var followers_count: String? = null,

    @field:SerializedName("following_count")
    var following_count: String? = null
)