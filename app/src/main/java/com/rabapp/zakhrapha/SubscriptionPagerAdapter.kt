package com.rabapp.zakhrapha

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class SubscriptionPagerAdapter (private val context:
                                Context, private val data: List<Subscriptviewpager>) :
    RecyclerView.Adapter<SubscriptionPagerAdapter.ViewHolder>() {
    private var selectedItemPosition: Int = -1
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewpager)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.subscribtion_viewpager_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        //holder.textViewTitle.text = item.title
       // holder.textViewDescription.text = item.description
        // Set image resource or URL
        holder.imageView.setImageResource(item.imageResId)

        //holder.imageView.setImageResource(item.circleImageResId)
       // holder.circleImageView.setImageResource(item.circleImageResId) // Use appropriate method to load images
    }

    override fun getItemCount(): Int = data.size
}