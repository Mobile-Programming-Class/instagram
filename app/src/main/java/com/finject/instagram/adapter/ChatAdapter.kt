package com.finject.instagram.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finject.instagram.R
import com.finject.instagram.data.ChatContent
import com.finject.instagram.data.GroupChat
import com.finject.instagram.interfaces.GalleryImageClickListener

class ChatAdapter (val activity: Context, val chatList: ArrayList<ChatContent>, var id_user: Int?) : RecyclerView.Adapter<ChatAdapter.ViewHolder>()  {

    var listener : GalleryImageClickListener?= null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        var layout_type = R.layout.item_chat_me;
        if ( viewType == 1 ) layout_type = R.layout.item_chat;

        val view = LayoutInflater.from(viewGroup?.context).inflate(layout_type, viewGroup, false)

        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name?.text = chatList[position].user?.name.toString()
        holder.description?.text = chatList[position].content.toString()

        Glide.with(activity)
            .load(chatList[position].user?.avatar)
            .into(holder.profile_img)
    }

    override fun getItemViewType(position: Int): Int {
        if ( chatList[position].user?.id == id_user ) {
            return 0;
        } else return 1;
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.brand_name)
        val profile_img = itemView.findViewById<ImageView>(R.id.profile_img)
        val description = itemView.findViewById<TextView>(R.id.description_txt)
    }
}
