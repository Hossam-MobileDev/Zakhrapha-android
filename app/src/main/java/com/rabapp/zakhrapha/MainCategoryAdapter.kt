package com.rabapp.zakhrapha

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

internal class MainCategoryAdapter(private val zakhareflist: ArrayList<HomeRecyler>,
    private val onTabClick:(Int)->Unit) :
    RecyclerView.Adapter<MainCategoryAdapter.MyViewHolder>() {
    private var selectedPosition = 0

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
val zakharefimg : ImageView =view.findViewById(R.id.zakarefimgid)
     val zakhareftext: TextView = view.findViewById(R.id.zakharefidtxt)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_view, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return zakhareflist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
val zakharef = zakhareflist[position]
       holder.zakharefimg.setImageResource(zakharef.zakharefImage)
     holder.zakhareftext.text = zakharef.zakharefName
        holder.itemView.setBackgroundResource(zakharef.colorzakharef);
        if (position == selectedPosition) {
            holder.zakhareftext.setTextColor(Color.BLACK)
            holder.zakharefimg.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
            // Highlight color
        } else {
            holder.zakhareftext.setTextColor(Color.WHITE)

            holder.zakharefimg.colorFilter = null
        }
        holder.itemView.setOnClickListener {
            selectedPosition = position // Update selected position
            notifyDataSetChanged() // Refresh the RecyclerView
            onTabClick(position) // Notify click event
        }

    }
    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        //notifyDataSetChanged()
    }
}