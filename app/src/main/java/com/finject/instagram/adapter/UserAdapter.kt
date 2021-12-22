package com.finject.instagram.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finject.instagram.R
import com.finject.instagram.data.User
import com.finject.instagram.interfaces.GalleryImageClickListener
import com.finject.instagram.interfaces.LikeListener

class UserAdapter (val activity: Context, val userList: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    var listener: GalleryImageClickListener? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0?.context).inflate(R.layout.item_search, p0, false)
        return ViewHolder(view);
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        p0.tvName?.text = userList[p1].name
        p0.tvId?.text = userList[p1].id.toString()

        Glide.with(activity)
            .load(userList[p1].avatar)
            .into(p0.ivIcon)

        // adding click or tap handler for our image layout
        p0.ivIcon.setOnClickListener {
            listener?.onClick(p1)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName = itemView.findViewById<TextView>(R.id.tvName)
        val tvId = itemView.findViewById<TextView>(R.id.tvId)
        val ivIcon = itemView.findViewById<ImageView>(R.id.ivIcon)
    }
}