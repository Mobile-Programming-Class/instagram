package com.finject.instagram.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finject.instagram.MainActivity
import com.finject.instagram.R
import com.finject.instagram.Refresh
import com.finject.instagram.adapter.PostAdapter
import com.finject.instagram.adapter.StatusAdapter
import com.finject.instagram.data.Post
import com.finject.instagram.data.Status
import com.google.gson.Gson

class HomeFragment(private var thisContext: MainActivity) : Fragment(), Refresh {

    val statusList = ArrayList<Status>()
    val postList = ArrayList<Post>()

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_home, parent, false)

        val activity = activity as Context
        thisContext.refreshListener = this

        val instaStausList = view.findViewById<RecyclerView>(R.id.insta_status_list)
        val postViewList = view.findViewById<RecyclerView>(R.id.post_list)

        instaStausList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        postViewList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        val statusJSON: String = activity.assets.open("status.json").bufferedReader().use { it.readText() }
        val postJSON: String = activity.assets.open("post.json").bufferedReader().use { it.readText() }

        val status = Gson().fromJson(statusJSON, Array<Status>::class.java)
        val post = Gson().fromJson(postJSON, Array<Post>::class.java)


        for (i in 0 until status.size)
            statusList.add(Status(status[i].id, status[i].name, status[i].picture))

        for (j in 0 until post.size)
            postList.add(Post(post[j].id, post[j].name, post[j].logo, post[j].photo, post[j].likes, post[j].description))

        val statusAdapter = StatusAdapter(activity,statusList)
        instaStausList.adapter = statusAdapter

        val postAdapter = PostAdapter(activity, postList)
        postViewList.adapter = postAdapter

        return view
    }

    override fun refresh () {
        // 1. clear postList, clear statusList
        // 2. call API data
        // 3. insert to adapter
        // 4. notifyDatasetChange

        statusList.clear()
        postList.clear()
    }
}