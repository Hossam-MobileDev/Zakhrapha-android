package com.rabapp.zakhrapha

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

internal class ShapesAdapter(private val names: Array<String>,

                             private val icons: Array<String>,
                             private val itemcliclistner: (Int)->Unit) :
    RecyclerView.Adapter<ShapesAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//var textContent : TextView =itemView.findViewById(R.id.text_content);

        var textname :TextView = itemView.findViewById(R.id.lblName);
        var texttitle :TextView = itemView.findViewById(R.id.lblTitle);

    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shape, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return names.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item: String = names.get(position)
        holder.textname.setText(item)
        val itemtitle: String = icons.get(position)
        holder.texttitle.setText(itemtitle)
        holder.itemView.setOnClickListener { itemcliclistner(position) }




    }

}