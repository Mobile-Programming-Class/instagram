package com.finject.instagram.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finject.instagram.MainActivity
import com.finject.instagram.PostActivity

import com.finject.instagram.R
import com.finject.instagram.adapter.FavouriteAdapter
import com.finject.instagram.adapter.PostAdapter
import com.finject.instagram.adapter.StatusAdapter
import com.finject.instagram.data.PostGet
import com.finject.instagram.data.ResponsePostingGetAll
import com.finject.instagram.data.Status
import com.finject.instagram.data.User
import com.finject.instagram.interfaces.GalleryImageClickListener
import com.finject.instagram.service.DataServices
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavouriteFragment(var thisContext: MainActivity ?= null) : Fragment(), GalleryImageClickListener {

    var favouritesList = ArrayList<PostGet>()
    lateinit var favouritesAdapter : FavouriteAdapter

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favourite, parent, false)

        val activity = activity as Context

        val recyclerFavourites = view.findViewById<RecyclerView>(R.id.recyclerFavourites)
        recyclerFavourites.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        favouritesAdapter = FavouriteAdapter(activity, favouritesList)
        favouritesAdapter.listener = this
        recyclerFavourites.adapter = favouritesAdapter

        val refresh = view.findViewById<TextView>( R.id.refresh )
        refresh.setOnClickListener { callApi() }

        callApi()

        return view
    }

    fun callApi() {
        if (thisContext?.access_token == null) return

        val networkServices = DataServices.create()
        val call = networkServices.getFavouritesPosts("Bearer " + thisContext?.access_token)

        call.enqueue(object: Callback<ResponsePostingGetAll> {
            override fun onFailure(call: Call<ResponsePostingGetAll>, t: Throwable) {
                println("On Failure")
                println(t.message)
                Toast.makeText(thisContext?.applicationContext, "Failed Getting Response" + t.message,
                    Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<ResponsePostingGetAll>, response: Response<ResponsePostingGetAll>) {
                Toast.makeText(thisContext?.applicationContext, "Success Getting Response",
                    Toast.LENGTH_LONG).show()
                if (response.body() != null) {
                    val data: ResponsePostingGetAll = response.body()!!
                    Toast.makeText(thisContext?.applicationContext, "Response body not null",
                        Toast.LENGTH_LONG).show()
                    if (data.data!!.isNotEmpty()) {
                        favouritesList.clear()
                        for (post in data.data!!) {
                            favouritesList.add(PostGet(
                                post?.id,
                                data.message + post?.foto,
                                post?.caption,
                                post?.id_user,
                                post?.created_at,
                                post?.updated_at,
                                post?.sentiment,
                                User(post?.user?.id,
                                    post?.user?.name,
                                    post?.user?.email,
                                    post?.user?.email_verified_at,
                                    post?.user?.bio,
                                    post?.user?.mobile,
                                    post?.user?.city,
                                    post?.user?.created_at,
                                    post?.user?.updated_at,
                                    data.message + post?.user?.avatar),
                                post?.like_count))
                        }
                        if (favouritesList.count() != 0) {
                            println("Dataset isnt null")
                            favouritesAdapter.notifyDataSetChanged()
                        }
                    }
                } else {
                    Toast.makeText(thisContext?.applicationContext, "Response Body Null on get fun",
                        Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onClick(position: Int) {
        val intent = Intent( thisContext!!, PostActivity::class.java)
        intent.putExtra("post_id", favouritesList[position].id.toString())
        intent.putExtra("post_foto", favouritesList[position].foto.toString())
        intent.putExtra("post_caption", favouritesList[position].caption.toString())
        intent.putExtra("user_name", favouritesList[position].user?.name.toString())
        intent.putExtra("user_avatar", favouritesList[position].user?.avatar.toString())
        intent.putExtra("access_token", thisContext?.access_token.toString() )
        startActivity(intent)
    }
}