package com.finject.instagram.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.finject.instagram.R
import com.finject.instagram.data.Post
import com.finject.instagram.interfaces.GalleryImageClickListener

class GalleryImageAdapter(private val itemList: List<Post>) : RecyclerView.Adapter<GalleryImageAdapter.ViewHolder>() {
    private var context: Context? = null
    var listener: GalleryImageClickListener? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivGalleryImage: ImageView

        init {
            ivGalleryImage = view.findViewById(R.id.ivGalleryImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_gallery, parent,
            false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageLink = itemList[position].foto

        // load imageLink
        Glide.with(context!!)
            .load(imageLink)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.ivGalleryImage)

        // adding click or tap handler for our image layout
        holder.ivGalleryImage.setOnClickListener {
            listener?.onClick(position)
        }
    }
}