package com.finject.instagram.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finject.instagram.MainActivity
import com.finject.instagram.PostActivity
import com.finject.instagram.R
import com.finject.instagram.Refresh
import com.finject.instagram.adapter.GalleryImageClickListener
import com.finject.instagram.adapter.PostAdapter
import com.finject.instagram.adapter.StatusAdapter
import com.finject.instagram.data.*
import com.finject.instagram.service.DataServices
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment(private var thisContext: MainActivity) : Fragment(), Refresh, GalleryImageClickListener {

    val statusList = ArrayList<Status>()
    val postList = ArrayList<PostGet>()
    lateinit var instaStatusList : RecyclerView
    lateinit var postAdapter : PostAdapter

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_home, parent, false)

        val activity = activity as Context
        thisContext.refreshListener = this

        instaStatusList = view.findViewById<RecyclerView>(R.id.insta_status_list)
        val postViewList = view.findViewById<RecyclerView>(R.id.post_list)

        instaStatusList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        postViewList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        val statusJSON: String = activity.assets.open("status.json").bufferedReader().use { it.readText() }
//        val postJSON: String = activity.assets.open("post.json").bufferedReader().use { it.readText() }

        val status = Gson().fromJson(statusJSON, Array<Status>::class.java)
//        val post = Gson().fromJson(postJSON, Array<Post>::class.java)


        for (i in 0 until status.size)
            statusList.add(Status(status[i].id, status[i].name, status[i].picture))

//        for (j in 0 until post.size)
//            postList.add(Post(post[j].id, post[j].name, post[j].logo, post[j].photo, post[j].likes, post[j].description))

        val statusAdapter = StatusAdapter(activity,statusList)
        instaStatusList.adapter = statusAdapter

        postAdapter = PostAdapter(activity, postList)
        postAdapter.listener = this
        postViewList.adapter = postAdapter

        return view
    }

    override fun refresh () {
        // 1. clear postList, clear statusList
        // 2. call API data
        // 3. insert to adapter
        // 4. notifyDatasetChange

        statusList.clear()
        if (thisContext.user != null) {
            statusList[0] = Status(thisContext.user?.id!!, thisContext.user?.name!!, thisContext.user?.avatar!!)
            instaStatusList.adapter?.notifyDataSetChanged()
        }



        val networkServices = DataServices.create()
        val call = networkServices.getAllPosts()

        call.enqueue(object: Callback<ResponsePostingGetAll> {
            override fun onFailure(call: Call<ResponsePostingGetAll>, t: Throwable) {
                println("On Failure")
                println(t.message)
                Toast.makeText(thisContext.applicationContext, "Failed Getting Response" + t.message,
                    Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<ResponsePostingGetAll>, response: Response<ResponsePostingGetAll>) {
                Toast.makeText(thisContext.applicationContext, "Success Getting Response",
                    Toast.LENGTH_LONG).show()
                if (response.body() != null) {
                    val data: ResponsePostingGetAll = response.body()!!
                    Toast.makeText(thisContext.applicationContext, "Response body not null",
                        Toast.LENGTH_LONG).show()
                    if (data.data!!.isNotEmpty()) {
                        postList.clear()
                        for (post in data.data!!) {
                            postList.add(PostGet(
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
                        if (postList.count() != 0) {
                            println("Dataset isnt null")
                            postAdapter.notifyDataSetChanged()
                        }
                    }
                } else {
                    Toast.makeText(thisContext.applicationContext, "Response Body Null on get fun",
                        Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onClick(position: Int) {
        val intent = Intent(thisContext, PostActivity::class.java)
        intent.putExtra("post_id", postList[position].id.toString())
        intent.putExtra("post_foto", postList[position].foto.toString())
        intent.putExtra("post_caption", postList[position].caption.toString())
        intent.putExtra("likes", postList[position].like_count.toString())
        intent.putExtra("user_name", postList[position].user?.name.toString())
        intent.putExtra("user_avatar", postList[position].user?.avatar.toString())
        startActivity(intent)
    }
}