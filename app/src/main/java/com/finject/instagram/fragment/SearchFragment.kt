package com.finject.instagram.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.finject.instagram.MainActivity

import com.finject.instagram.R
import com.finject.instagram.adapter.PostAdapter
import com.finject.instagram.adapter.UserAdapter
import com.finject.instagram.data.*
import com.finject.instagram.service.DataServices
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment(var thisContext: MainActivity) : Fragment() {

    val userList = ArrayList<User>()
    var etSearch : EditText ?= null
    lateinit var userAdapter : UserAdapter

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, parent, false)
        initItem(view)

        val activity = activity as Context

        val userViewList = view.findViewById<RecyclerView>(R.id.rvImage)
        userAdapter = UserAdapter(activity, userList)
//        userAdapter.listener = this
        userViewList.adapter = userAdapter

        return view
    }

    fun initItem(view: View) {
        etSearch = view.findViewById<EditText>( R.id.etSearch )
        val btnSearch = view.findViewById<ImageButton>( R.id.btnSearch )
        val rvImage = view.findViewById<RecyclerView>( R.id.rvImage )

        btnSearch.setOnClickListener { searchUser() }
    }

    fun searchUser() {

        val networkServices = DataServices.create()
        val call = networkServices.getUserByKeyword( etSearch?.text.toString() )

        call.enqueue(object: Callback<ResponseUserByKeyword> {
            override fun onFailure(call: Call<ResponseUserByKeyword>, t: Throwable) {
                println("On Failure")
                println(t.message)
                Toast.makeText(thisContext?.applicationContext, "Failed Getting Response" + t.message,
                    Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<ResponseUserByKeyword>, response: Response<ResponseUserByKeyword>) {
                Toast.makeText(thisContext?.applicationContext, "Success Getting Response",
                    Toast.LENGTH_LONG).show()
                if (response.body() != null) {
                    val data: ResponseUserByKeyword = response.body()!!
                    Toast.makeText(thisContext?.applicationContext, "Response body not null",
                        Toast.LENGTH_LONG).show()
                    if (data.data!!.isNotEmpty()) {
                        userList.clear()
                        for (user in data.data!!) {
                            userList.add(
                                User(user?.id,
                                    user?.name,
                                    user?.email,
                                    user?.email_verified_at,
                                    user?.bio,
                                    user?.mobile,
                                    user?.city,
                                    user?.created_at,
                                    user?.updated_at,
                                    data.message + user?.avatar))
                        }
                        if (userList.count() != 0) {
                            println("Dataset isnt null")
                            userAdapter.notifyDataSetChanged()
                        }

                    }
                } else {
                    Toast.makeText(thisContext?.applicationContext, "Response Body Null on get fun",
                        Toast.LENGTH_LONG).show()
                }
            }
        })
    }

}