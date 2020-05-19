package com.sunil.wallyapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sunil.wallyapp.R
import com.sunil.wallyapp.data.model.Photos
import kotlinx.android.synthetic.main.item_photo.view.*

class PhotoAdapter(var photoList: ArrayList<Photos>) :
    RecyclerView.Adapter<PhotoAdapter.ContentViewHolder>() {

    fun updateContent(content: List<Photos>) {
        photoList.clear()
        photoList.addAll(content)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = ContentViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
    )

    override fun getItemCount() = photoList.size

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bind(photoList[position], holder.itemView.context)
    }

    class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
       // private val title = view.title
        private val imageView = view.imageView
        private  val profileImage = view.imageView2

        fun bind(photos: Photos, context: Context) {
            //title.text = content.name
            val url = photos.urls?.regular
            val profileUrl = photos.user?.profileImage?.medium

            if (url != null && url.isNotEmpty()) {
                Glide.with(context)
                    .load(url)
                    .into(imageView)

            }
            if (profileUrl != null && profileUrl.isNotEmpty()) {
                Glide.with(context)
                    .load(profileUrl)
                    .into(profileImage)
            }

        }
    }
}