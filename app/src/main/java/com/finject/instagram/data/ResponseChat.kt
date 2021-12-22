package com.finject.instagram.data

import com.google.gson.annotations.SerializedName

data class ResponseGroupChat(

    @field:SerializedName("success")
    var success: Boolean? = null,

    @field:SerializedName("data")
    var data: MutableList<GroupChat?> ? = null,

    @field:SerializedName("message")
    var message: String? = null
)

data class ResponseSendChat (
    @field:SerializedName("success")
    var success: Boolean? = null,

    @field:SerializedName("data")
    var data: ChatContent ? = null,

    @field:SerializedName("message")
    var message: String? = null
)

data class ResponseChat (

    @field:SerializedName("success")
    var success: Boolean? = null,

    @field:SerializedName("data")
    var data: Chats ? = null,

    @field:SerializedName("message")
    var message: String? = null
)

data class Chats(
    @field:SerializedName("id")
    var id : Int?,

    @field:SerializedName("created_at")
    var created_at : String? = null,

    @field:SerializedName("updated_at")
    var updated_at : String? = null,

    @field:SerializedName("user")
    var user: MutableList<User?> ?= null,

    @field:SerializedName("chat_content")
    var chat_content : MutableList<ChatContent?> ? = null,

)

    /*
    "id": 5,
    "content": "hology",
    "id_chat": 3,
    "pengirim": 4,
    "created_at": "2021-12-22T03:47:17.000000Z",
    "updated_at": "2021-12-22T03:47:17.000000Z",
    "sentiment": null,
    "user":
     */
data class ChatContent(
    @field:SerializedName("id")
    var id : Int?,

    @field:SerializedName("content")
    var content : String? = null,

    @field:SerializedName("id_chat")
    var id_chat : Int?,

    @field:SerializedName("pengirim")
    var pengirim : Int?,

    @field:SerializedName("created_at")
    var created_at : String? = null,

    @field:SerializedName("updated_at")
    var updated_at : String? = null,

    @field:SerializedName("sentiment")
    var sentiment: Float? = null,

    @field:SerializedName("user")
    var user: User ?= null,
)

data class GroupChat(
    @field:SerializedName("id")
    var id : Int?,

    @field:SerializedName("created_at")
    var created_at : String? = null,

    @field:SerializedName("updated_at")
    var updated_at : String? = null,

    @field:SerializedName("user")
    var user: MutableList<User?> ?= null,
)