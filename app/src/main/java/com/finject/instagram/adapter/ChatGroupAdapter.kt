package com.finject.instagram.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finject.instagram.R
import com.finject.instagram.data.GroupChat
import com.finject.instagram.data.PostGet
import com.finject.instagram.interfaces.GalleryImageClickListener
import com.finject.instagram.interfaces.LikeListener

class ChatGroupAdapter (val activity: Context, val chatGroupList: MutableList<GroupChat>) : RecyclerView.Adapter<ChatGroupAdapter.ViewHolder>()  {

    var listener : GalleryImageClickListener ?= null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0?.context).inflate(R.layout.item_group_chat, p0, false)
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name?.text = chatGroupList[position].id.toString()
        holder.description?.text = "created by " + chatGroupList[position].user?.last()?.name


        // adding click or tap handler for our image layout
        holder.card.setOnClickListener {
            listener?.onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return chatGroupList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.brand_name)
        val profile_img = itemView.findViewById<ImageView>(R.id.profile_img)
        val description = itemView.findViewById<TextView>(R.id.description_txt)
        val card = itemView.findViewById<CardView>( R.id.card )
    }
}