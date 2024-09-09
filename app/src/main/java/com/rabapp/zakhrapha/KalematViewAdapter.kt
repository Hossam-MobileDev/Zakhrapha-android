package com.rabapp.zakhrapha

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

internal class KalematViewAdapter
    (private var itemsList: List<String>,private val onClick: (Int) -> Unit) :
    RecyclerView.Adapter<KalematViewAdapter.MyViewHolder>() {
    private val favoriteItems: MutableSet<String> = mutableSetOf()

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemTextView: TextView = view.findViewById(R.id.lblName)
var poimage :ImageView = view.findViewById(R.id.poemImage)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KalematViewAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_poems_menu, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val imageName = "cat${position + 1}" // Construct the image name dynamically
        val imageResId = holder.itemView.context.resources.getIdentifier(imageName, "drawable",holder.itemView.getContext().getPackageName())
        val item = itemsList[position]
        holder.itemView.setOnClickListener { onClick(position) }

        holder.itemTextView.text = item
        Glide.with(holder.poimage)
            .load(imageResId)
            .into(holder.poimage)
    }






    override fun getItemCount(): Int {
        return itemsList.size
    }
}