package com.finject.instagram

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finject.instagram.adapter.ChatAdapter
import com.finject.instagram.adapter.ChatMemberAdapter
import com.finject.instagram.adapter.StatusAdapter
import com.finject.instagram.data.*
import com.finject.instagram.interfaces.GalleryImageClickListener
import com.finject.instagram.service.DataServices
import com.finject.instagram.service.SessionManager
import org.tensorflow.lite.examples.textclassification.TextClassificationClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatActivity : AppCompatActivity(){

    private lateinit var sessionManager: SessionManager
    lateinit var refresh : TextView
    lateinit var profile_pic : ImageView
    lateinit var chat_content : EditText
    lateinit var sendBtn : ImageView

    var access_token : String ?= null
    var id_user : String ?= null
    var id_chat : String ?= null
    var avatar : String ?= null

    val chatList = ArrayList<ChatContent>()
    val userList = ArrayList<User>()
    lateinit var chatAdapter : ChatAdapter
    lateinit var userAdapter : ChatMemberAdapter

    lateinit var client: TextClassificationClient

    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        access_token = intent.getStringExtra("access_token").toString()
        id_user = intent.getStringExtra("id_user").toString()
        id_chat = intent.getStringExtra("id_chat").toString()
        avatar = intent.getStringExtra("avatar").toString()

        val recyclerChat = findViewById<RecyclerView>(R.id.recyclerChat)
        recyclerChat.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        chatAdapter = ChatAdapter(this, chatList, id_user?.toInt())
        recyclerChat.adapter = chatAdapter



        val recyclerChatMember = findViewById<RecyclerView>(R.id.recyclerChatMember)
        recyclerChatMember.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        userAdapter = ChatMemberAdapter(this, userList)
        recyclerChatMember.adapter = userAdapter

        client = TextClassificationClient(this)
        client.load()

        sessionManager = SessionManager( this )
        initItem()
    }

    fun initItem() {
        refresh = findViewById( R.id.refresh )
        sendBtn = findViewById( R.id.sendBtn )
        profile_pic = findViewById( R.id.profile_pic )
        chat_content = findViewById( R.id.chat_content )

        loadGroupChat()

        refresh.setOnClickListener { loadGroupChat() }
        Glide.with(this)
            .load( avatar )
            .into( profile_pic )

        sendBtn.setOnClickListener { sendChat() }
    }

    fun sendChat() {
        if ( (access_token == null || access_token.equals("")) && sessionManager.fetchAuthToken() == null ) {
            Toast.makeText(getApplicationContext(), "Please Login First",
                Toast.LENGTH_LONG).show()
            return
        }

        val networkServices = DataServices.create()
        val call = networkServices.sendChat( "Bearer " + access_token, chat_content?.text.toString(), id_chat!!, predictIt( chat_content?.text.toString() ).toString() )

        call.enqueue(object: Callback<ResponseSendChat> {
            override fun onFailure(call: Call<ResponseSendChat>, t: Throwable) {
                println("On Failure")
                println(t.message)
//                Toast.makeText(getApplicationContext(), "Failed Getting Response" + t.message,
//                    Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<ResponseSendChat>, response: Response<ResponseSendChat>) {
//                Toast.makeText(getApplicationContext(), "Success Getting Response",
//                    Toast.LENGTH_LONG).show()
                if (response.body() != null) {

                    loadGroupChat()


                } else {
//                    Toast.makeText(getApplicationContext(), "Response Body Null on get fun",
//                        Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun predictIt(caption: String): Float? {

        val results = client.classify(caption)
        Toast.makeText(this, "Success predict " + results[0].confidence.toString(),
            Toast.LENGTH_LONG).show()

        var res : Float ? = null
        for(result in results){
            Toast.makeText(this, result.toString(),
                Toast.LENGTH_LONG).show()
            if ( result.title.equals("Positive") )
                res = result.confidence
        }

        return res
    }

    fun loadGroupChat() {
        if ( (access_token == null || access_token.equals("")) && sessionManager.fetchAuthToken() == null ) {
            Toast.makeText(getApplicationContext(), "Please Login First",
                Toast.LENGTH_LONG).show()
            return
        }

        val networkServices = DataServices.create()
        val call = networkServices.getChats( "Bearer " + access_token, id_chat )

        call.enqueue(object: Callback<ResponseChat> {
            override fun onFailure(call: Call<ResponseChat>, t: Throwable) {
                println("On Failure")
                println(t.message)
//                Toast.makeText(getApplicationContext(), "Failed Getting Response" + t.message,
//                    Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<ResponseChat>, response: Response<ResponseChat>) {
//                Toast.makeText(getApplicationContext(), "Success Getting Response",
//                    Toast.LENGTH_LONG).show()
                if (response.body() != null) {
                    val data: ResponseChat = response.body()!!
//                    Toast.makeText(getApplicationContext(), "Response body not null",
//                        Toast.LENGTH_LONG).show()
                    if (data.data!!.user!!.isNotEmpty()) {
                        userList.clear()
                        for (user in data.data!!.user!!) {
                            userList.add( User(user?.id,
                                user?.name,
                                user?.email,
                                user?.email_verified_at,
                                user?.bio,
                                user?.mobile,
                                user?.city,
                                user?.created_at,
                                user?.updated_at,
                                data.message + user?.avatar),)
                        }
                        if (userList.count() != 0) {
                            println("Dataset isnt null")
                            userAdapter.notifyDataSetChanged()
                        }
                    }
                    if (data.data!!.chat_content!!.isNotEmpty()) {
                        chatList.clear()
                        for (chat_content in data.data!!.chat_content!!) {
                            chatList.add( ChatContent(
                                chat_content?.id,
                                chat_content?.content,
                                chat_content?.id_chat,
                                chat_content?.pengirim,
                                chat_content?.created_at,
                                chat_content?.updated_at,
                                chat_content?.sentiment,
                                User (
                                    chat_content?.user?.id,
                                    chat_content?.user?.name,
                                    chat_content?.user?.email,
                                    chat_content?.user?.email_verified_at,
                                    chat_content?.user?.bio,
                                    chat_content?.user?.mobile,
                                    chat_content?.user?.city,
                                    chat_content?.user?.created_at,
                                    chat_content?.user?.updated_at,
                                    data.message + chat_content?.user?.avatar,
                                )
                            ) )
                        }
                        if (chatList.count() != 0) {
                            println("Dataset isnt null")
                            chatAdapter.notifyDataSetChanged()
                        }
                    }

                } else {
//                    Toast.makeText(getApplicationContext(), "Response Body Null on get fun",
//                        Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}