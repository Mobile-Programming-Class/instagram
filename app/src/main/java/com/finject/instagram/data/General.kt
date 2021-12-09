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