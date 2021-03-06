package com.finject.instagram.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.finject.instagram.data.PostGet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.finject.instagram.interfaces.LikeListener

import com.finject.instagram.R
import com.finject.instagram.interfaces.GalleryImageClickListener

class PostAdapter (val activity: Context, val postList: ArrayList<PostGet>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    var listener: GalleryImageClickListener? = null
    var listenerLike : LikeListener? =null
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0?.context).inflate(R.layout.adapter_post_layout, p0, false)
        return ViewHolder(view);
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        p0.name?.text = postList[p1].user?.name
        p0.likes?.text = postList[p1].like_count!!.toString() +" "+"likes"
        p0.description?.text = postList[p1].caption

        Glide.with(activity)
            .load(postList[p1].user?.avatar)
            .into(p0.logo)

        Glide.with(activity)
            .load(postList[p1].foto)
            .into(p0.photo)


        // adding click or tap handler for our image layout
        p0.photo.setOnClickListener {
            listener?.onClick(p1)
        }

        p0.heart.setOnClickListener {
            listenerLike?.onClickLikeIt( postList[ p1 ].id!! )
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.brand_name)
        val logo = itemView.findViewById<ImageView>(R.id.logo)
        val photo = itemView.findViewById<ImageView>(R.id.post_img)
        val likes = itemView.findViewById<TextView>(R.id.likes_txt)
        val description = itemView.findViewById<TextView>(R.id.description_txt)

        val heart = itemView.findViewById<ImageView>(R.id.heart)
    }
}