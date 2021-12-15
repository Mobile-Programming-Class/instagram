package com.finject.instagram.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.finject.instagram.MainActivity

import com.finject.instagram.R
import com.finject.instagram.Refresh
import com.finject.instagram.adapter.GalleryImageAdapter
import com.finject.instagram.adapter.GalleryImageClickListener
import com.finject.instagram.data.Follower
import com.finject.instagram.data.Post
import com.finject.instagram.data.ResponsePostingGetById
import com.finject.instagram.service.DataServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment(var thisContext: MainActivity) : Fragment(), GalleryImageClickListener, Refresh {

    private val SPAN_COUNT = 3
    private val imageList = ArrayList<Post>()
    lateinit var galleryAdapter: GalleryImageAdapter

    var thisView : View ? = null

    lateinit var icLogin : ImageView
    lateinit var ivAvatarProfile : ImageView
    lateinit var tv_name : TextView
    lateinit var tvAddress : TextView
    lateinit var tvFollowersCount : TextView
    lateinit var tvFollowingCount : TextView
    lateinit var tvEmailProfile : TextView
    lateinit var tvPhoneNumber : TextView
    lateinit var tvBio : TextView

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, parent, false)
        thisView = view
        // init adapter
        galleryAdapter = GalleryImageAdapter(imageList)
        galleryAdapter.listener = this
        thisContext.refreshListener = this

        initItem();


        return view
    }

    fun initItem() {
        icLogin = thisView!!.findViewById<ImageView>(R.id.icLogin)
        ivAvatarProfile = thisView!!.findViewById<ImageView>(R.id.ivAvatarProfile)
        tv_name = thisView!!.findViewById<TextView>(R.id.tv_name)
        tvFollowersCount = thisView!!.findViewById<TextView>(R.id.tvFollowersCount)
        tvFollowingCount = thisView!!.findViewById<TextView>(R.id.tvFollowingCount)
        tvEmailProfile = thisView!!.findViewById<TextView>(R.id.tvEmailProfile)
        tvPhoneNumber = thisView!!.findViewById<TextView>(R.id.tvPhoneNumber)
        tvBio = thisView!!.findViewById<TextView>(R.id.tvBio)
        setFollowCount()

        Glide.with(thisContext)
            .load(thisContext.user?.avatar)
            .into(ivAvatarProfile)

        tv_name.text = thisContext.user?.name
        tvEmailProfile.text = thisContext.user?.email
        tvPhoneNumber.text = thisContext.user?.mobile
        tvBio.text = thisContext.user?.bio
    }

    fun setFollowCount() {
        val networkServices = DataServices.create()

        val callPost = networkServices.getFollowersById( thisContext.user?.id.toString() )
        callPost.enqueue(object: Callback<Follower> {
            override fun onFailure(call: Call<Follower>, t: Throwable) {
                println("On Failure")
                println(t.message)
                Toast.makeText(thisContext, "Failed Getting Response" + t.message,
                    Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<Follower>, response: Response<Follower>) {
                Toast.makeText(thisContext, "Success Getting Response",
                    Toast.LENGTH_LONG).show()
                if (response.body() != null) {
                    val data: Follower = response.body()!!
                    Toast.makeText(thisContext, "Response body not null",
                        Toast.LENGTH_LONG).show()
                    if (data.data != null) {
                        thisContext.follower = data.data
                        tvFollowersCount.text = thisContext.follower?.followers_count
                        tvFollowingCount.text = thisContext.follower?.following_count
                    } else {
                        Toast.makeText(thisContext, "Response NOT null. Post IS null",
                            Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(thisContext, "Response Body Null",
                        Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun refresh() {
        setFollowCount()
    }

    override fun onClick(position: Int){}

}