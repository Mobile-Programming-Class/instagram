package com.finject.instagram

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finject.instagram.adapter.ChatGroupAdapter
import com.finject.instagram.adapter.PostAdapter
import com.finject.instagram.data.*
import com.finject.instagram.interfaces.GalleryImageClickListener
import com.finject.instagram.service.DataServices
import com.finject.instagram.service.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatGroupActivity : AppCompatActivity(), GalleryImageClickListener {

    private lateinit var sessionManager: SessionManager
    lateinit var refresh : Button
    lateinit var btnCreateGroup : Button
    lateinit var etMemberInput : EditText

    var access_token : String ?= null
    var id_user : String ?= null
    var avatar : String ?= null

    val chatGroupList = ArrayList<GroupChat>()
    lateinit var chatGroupAdapter : ChatGroupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_group)

        val recyclerChatGroup = findViewById<RecyclerView>(R.id.recyclerChatGroup)
        recyclerChatGroup.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        chatGroupAdapter = ChatGroupAdapter(this, chatGroupList)
        chatGroupAdapter.listener = this
        recyclerChatGroup.adapter = chatGroupAdapter

        access_token = intent.getStringExtra("access_token").toString()
        id_user = intent.getStringExtra("id_user").toString()
        avatar = intent.getStringExtra("avatar").toString()

        sessionManager = SessionManager( this )
        initItem()
    }

    fun initItem() {
        refresh = findViewById( R.id.refresh )
        btnCreateGroup = findViewById( R.id.btnCreateGroup )
        etMemberInput = findViewById( R.id.etMemberInput )

        loadGroupChat()

        refresh.setOnClickListener { loadGroupChat() }
        btnCreateGroup.setOnClickListener { createGroup() }
    }

    fun createGroup() {
        if ( (access_token == null || access_token.equals("")) && sessionManager.fetchAuthToken() == null ) {
            Toast.makeText(getApplicationContext(), "Please Login First",
                Toast.LENGTH_LONG).show()
            return
        }

        val networkServices = DataServices.create()
        val call = networkServices.createChatGroup( "Bearer " + access_token, etMemberInput.text.toString() )

        call.enqueue(object: Callback<ResponseGroupChat> {
            override fun onFailure(call: Call<ResponseGroupChat>, t: Throwable) {
                println("On Failure")
                println(t.message)
//                Toast.makeText(getApplicationContext(), "Failed Getting Response" + t.message,
//                    Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<ResponseGroupChat>, response: Response<ResponseGroupChat>) {
//                Toast.makeText(getApplicationContext(), "Success Getting Response",
//                    Toast.LENGTH_LONG).show()
                if (response.body() != null) {
                    val data: ResponseGroupChat = response.body()!!
                    Toast.makeText(getApplicationContext(), "Response body not null",
                        Toast.LENGTH_LONG).show()
                    if (data.data!!.isNotEmpty()) {

                        loadGroupChat()

                    }
                } else {
//                    Toast.makeText(getApplicationContext(), "Response Body Null on get fun",
//                        Toast.LENGTH_LONG).show()
                }
            }
        })


    }

    fun loadGroupChat() {
        if ( (access_token == null || access_token.equals("")) && sessionManager.fetchAuthToken() == null ) {
            Toast.makeText(getApplicationContext(), "Please Login First",
                Toast.LENGTH_LONG).show()
            return
        }

        val networkServices = DataServices.create()
        val call = networkServices.getChatGroup( "Bearer " + access_token )

        call.enqueue(object: Callback<ResponseGroupChat> {
            override fun onFailure(call: Call<ResponseGroupChat>, t: Throwable) {
                println("On Failure")
                println(t.message)
//                Toast.makeText(getApplicationContext(), "Failed Getting Response" + t.message,
//                    Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<ResponseGroupChat>, response: Response<ResponseGroupChat>) {
                Toast.makeText(getApplicationContext(), "Success Getting Response",
                    Toast.LENGTH_LONG).show()
                if (response.body() != null) {
                    val data: ResponseGroupChat = response.body()!!
//                    Toast.makeText(getApplicationContext(), "Response body not null",
//                        Toast.LENGTH_LONG).show()
                    if (data.data!!.isNotEmpty()) {
                        chatGroupList.clear()
                        for (groupChat in data.data!!) {
                            var userMutableList = ArrayList<User?>()
                            for ( user in groupChat?.user!! )
                                userMutableList.add( user )
                            chatGroupList.add(GroupChat(
                                groupChat.id,
                                groupChat.created_at,
                                groupChat.updated_at,
                                userMutableList)
                            )
                        }
                        if (chatGroupList.count() != 0) {
                            println("Dataset isnt null")
                            chatGroupAdapter.notifyDataSetChanged()
                        }
                    }
                } else {
//                    Toast.makeText(getApplicationContext(), "Response Body Null on get fun",
//                        Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onClick(position: Int) {
        val intent = Intent( this, ChatActivity::class.java)
        intent.putExtra("access_token", access_token.toString() )
        intent.putExtra("id_chat", chatGroupList[position].id.toString() )
        intent.putExtra("id_user", id_user?.toString() )
        intent.putExtra("avatar", avatar?.toString() )
        startActivity(intent)
    }
}