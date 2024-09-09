package com.rabapp.zakhrapha

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

internal class HistoryAdapter(
    private val historyItems: MutableList<HistoryItem>,
    private val onCopyClicked: (String) -> Unit,
     private val onHideClick: (Int) -> Unit,)
    :
    RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
var textContent : TextView =itemView.findViewById(R.id.txtelmodlarow);

        var textDate :TextView = itemView.findViewById(R.id.txtdate);
        var iconDelete :ImageView= itemView.findViewById(R.id.deleteimg);
        var iconCopy :ImageView= itemView.findViewById(R.id.copyIdImage);
        var iconhide: ImageView = itemView.findViewById(R.id.elsegelhide)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return historyItems?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item: HistoryItem? = historyItems?.get(position)
        holder.textContent.setText(item?.textContent)
        holder.textDate.setText(item?.date)
holder.iconhide.setOnClickListener {   onHideClick(position) }
        holder.iconCopy.setOnClickListener {
            onCopyClicked(item?.textContent.toString())
            // saveToHistory(item)
            //insertIntoHistory(item)
        }
        holder.itemView.setOnClickListener{
            onCopyClicked(item?.textContent.toString())
        }
        holder.iconDelete.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                // Safely remove item
                (historyItems as MutableList).removeAt(currentPosition)

                // Notify the adapter of item removed
                notifyItemRemoved(currentPosition)

                // Notify the adapter of items changed from current position onwards
                //notifyItemRangeChanged(currentPosition, historyItems?.size ?:0 )
            }
        }



    }

    fun updateItems(newItems: MutableList<HistoryItem>) {
        historyItems.clear()
        historyItems.addAll(newItems)
        notifyDataSetChanged()
    }



    fun removeItem(position: Int) {
        if (position >= 0 && position < historyItems.size) {
            historyItems.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, historyItems.size)
    }
}}