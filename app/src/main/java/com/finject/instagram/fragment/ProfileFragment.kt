package com.finject.instagram.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.finject.instagram.R
import com.finject.instagram.adapter.GalleryImageAdapter
import com.finject.instagram.adapter.GalleryImageClickListener
import com.finject.instagram.data.Post

class ProfileFragment : Fragment(), GalleryImageClickListener {

    private val SPAN_COUNT = 3
    private val imageList = ArrayList<Post>()
    lateinit var galleryAdapter: GalleryImageAdapter

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, parent, false)
        // init adapter
        galleryAdapter = GalleryImageAdapter(imageList)
        galleryAdapter.listener = this

        return view
    }

    override fun onClick(position: Int){}

}