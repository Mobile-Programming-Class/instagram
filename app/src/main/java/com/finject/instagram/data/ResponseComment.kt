package com.finject.instagram.data

import com.google.gson.annotations.SerializedName

//data class ResponseComment()

// see Comment
// http://127.0.0.1:8000/api/comment/by-post-id/44
// http://127.0.0.1:8000/api/comment/by-user-id/13


// http://127.0.0.1:8000/api/user/comment/create
// http://127.0.0.1:8000/api/user/comment/edit/10
data class ResponseComment(

    @field:SerializedName("success")
    var success: Boolean? = null,

    @field:SerializedName("data")
    var data: ItemComment? = null,

    @field:SerializedName("message")
    var message: String? = null
)

// http://127.0.0.1:8000/api/user/comment/delete/10 see general