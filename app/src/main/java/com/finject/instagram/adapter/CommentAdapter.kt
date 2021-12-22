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
import com.finject.instagram.data.ItemComment
import com.finject.instagram.data.PostGet

class CommentAdapter (val comment_list: ArrayList<ItemComment>) : RecyclerView.Adapter<CommentAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0?.context).inflate(R.layout.item_comment, p0, false)
        return ViewHolder(view);
    }

    override fun getItemCount(): Int {
        return comment_list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        p0.username?.text = comment_list[p1].user?.name
        p0.comment?.text =  comment_list[p1].isi // postList[p1].likes +" "+"likes"
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username = itemView.findViewById<TextView>(R.id.tvUserName)
        val comment = itemView.findViewById<TextView>(R.id.tvComment)
    }
}