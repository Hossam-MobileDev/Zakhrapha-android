package com.rabapp.zakhrapha

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

internal class FavouriteAdapter(
    private var favlist: MutableList<FavoriteItem>,
     // Pass the context to access SharedPreferences
    private val favoriteViewModel: HistoryViewModel
) :
    RecyclerView.Adapter<FavouriteAdapter.MyViewHolder>() {



    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
val copyImage : ImageView =view.findViewById(R.id.elmofdlaidimgcopy)
     val favouriteImage: ImageView = view.findViewById(R.id.favfaelmofdlaimg)
        var elmofdlatext:TextView = view.findViewById(R.id.txtelmofdla)

    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.elmofdlarow, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return favlist?.size ?: 0
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = favlist?.get(position)
        if (item != null) {
            holder.elmofdlatext.text = item.text

        }

        // Set up click listeners for favorite and copy buttons
        holder.favouriteImage.setOnClickListener {
            removeItem(holder.adapterPosition)
        }
        holder.copyImage.setOnClickListener {
           // onCopyClick(item)
        }
    }
    private  fun removeItem(position: Int) {
        val item = favlist?.get(position)

        // Check if the item is not null
        if (item != null) {
            // Remove the item from the database

                favoriteViewModel.deleteFavoriteItem(item)


            // Remove the item from the list (assuming favlist is a mutable list)
            favlist?.remove(item)

            // Notify the adapter about the item removal
            (favlist as? MutableList<*>)?.let {
                notifyItemRemoved(position)
            }
        }

    }


    // Method to update the list in the FavouriteAdapter
    fun updateList(newFavList: List<FavoriteItem?>?) {
        favlist.clear()
        // If newFavList is not null, filter out null elements and add to favlist
        newFavList?.filterNotNull()?.let {
            favlist.addAll(it)
        }
        notifyDataSetChanged() // Notify the adapter about data change
    }
}