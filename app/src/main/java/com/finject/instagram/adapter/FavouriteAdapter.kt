package com.finject.instagram.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finject.instagram.R
import com.finject.instagram.data.PostGet
import com.finject.instagram.interfaces.GalleryImageClickListener

class FavouriteAdapter (val activity: Context, val favouritesList: ArrayList<PostGet>) : RecyclerView.Adapter<FavouriteAdapter.ViewHolder>()  {

    var listener : GalleryImageClickListener ?= null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0?.context).inflate(R.layout.item_favourites, p0, false)
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name?.text = favouritesList[position].user?.name
        holder.description?.text = favouritesList[position].caption

        Glide.with(activity)
            .load(favouritesList[position].user?.avatar)
            .into(holder.profile_img)

        Glide.with(activity)
            .load(favouritesList[position].foto)
            .into(holder.photo)


        // adding click or tap handler for our image layout
        holder.photo.setOnClickListener {
            listener?.onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return favouritesList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.brand_name)
        val profile_img = itemView.findViewById<ImageView>(R.id.profile_img)
        val photo = itemView.findViewById<ImageView>(R.id.post_img)
        val description = itemView.findViewById<TextView>(R.id.description_txt)
    }
}