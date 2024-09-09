package com.rabapp.zakhrapha

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

internal class AshkalnewViewAdapter
    (private var itemsList: List<String>
    ,private val arrMenuIcon: List<String>,private val onClick: (Int) -> Unit):

    RecyclerView.Adapter<AshkalnewViewAdapter.MyViewHolder>() {
    private val maxSize = maxOf(itemsList.size, arrMenuIcon.size)

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var texttitle : TextView= view.findViewById(R.id.title)
        var texticon :TextView = view.findViewById(R.id.iconnamee);

    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AshkalnewViewAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ashkal, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val text1 = if (position < itemsList.size) itemsList[position] else ""
        val text2 = if (position < arrMenuIcon.size) arrMenuIcon[position] else ""
        // val color = ContextCompat.getColor(geconte(), R.color.edittextcolor)

        //holder.cardview.setCardBackgroundColor(reso.getColor(R.color.edittextcolor))
        holder.itemView.setOnClickListener { onClick(position) }

        holder.texttitle.setText(text1)

        holder.texticon.setText(text2)
    }






    override fun getItemCount(): Int {
        return itemsList.size
    }
}