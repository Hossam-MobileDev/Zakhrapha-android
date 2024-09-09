package com.rabapp.zakhrapha

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class ElashkalDetailAdapter (   private val items: List<String>, private val favoriteViewModel: HistoryViewModel,
                                         private val onFavoriteClicked: (String) -> Unit,
                                         private val onCopyClicked: (String) -> Unit,) :
    RecyclerView.Adapter<ElashkalDetailAdapter.MyViewHolder>() {
    private val favoriteItems: MutableSet<String> = mutableSetOf()

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iconfavorite :ImageView= itemView.findViewById(R.id.favoriteIdImage);
        var textaskal :TextView = itemView.findViewById(R.id.txtelmodlarow);
        var iconCopy :ImageView= itemView.findViewById(R.id.copyIdimage);
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.zakhrfarow, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items.get(position)
        holder.textaskal.setText(item)

        holder.iconCopy.setOnClickListener{

            onCopyClicked(item)
        }
        holder.itemView.setOnClickListener{
            onCopyClicked(item)
        }
        updateFavoriteButton(holder.iconfavorite, item)

        holder.iconfavorite.setOnClickListener{
            toggleFavorite(item)
            onFavoriteClicked(item)
        }


    }
    private fun updateFavoriteButton(fovoriteImageitem: ImageView, item: String) {
        val isFavorite = favoriteItems.contains(item)
        fovoriteImageitem.setImageResource(
            if (isFavorite) R.drawable.heartred else R.drawable.group_26
        )
    }
    fun toggleFavorite(item: String) {
        if (favoriteItems.contains(item)) {
            // If the item is already a favorite, remove it
            favoriteItems.remove(item)
            removeFavoriteFromDatabase(item) // Use ViewModel to delete from the database
        } else {
            // If the item is not a favorite, add it
            favoriteItems.add(item)
            insertIntoFavorite(item) // Use ViewModel to insert into the database
        }
        // saveFavoritesToPreferences() // Optional: Save to preferences if needed
        notifyItemChanged(items.indexOf(item)) // Update the changed item in the UI
    }
    private fun removeFavoriteFromDatabase(text: String) {
        // Use the ViewModel to perform the delete operation
        CoroutineScope(Dispatchers.IO).launch {
            // Fetch the favorite item by its text
            val favItem = FavoriteItem(text = text)
            favoriteViewModel.deleteFavoriteItem(favItem) // Use ViewModel to delete the item

        }
    }
    private fun insertIntoFavorite(text: String) {
        // Format the current date
        //  val currentDate = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(Date())

        // Use a coroutine to insert the item into the database

        val favItem = FavoriteItem(text = text)
        val newId = favoriteViewModel.insertFavoriteItem(favItem) // Handle the ID if needed
        Log.d("FavoriteViewModel", "Inserted item with ID: $newId")

    }

}