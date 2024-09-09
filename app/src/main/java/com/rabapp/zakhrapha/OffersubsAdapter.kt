package com.rabapp.zakhrapha

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OffersubsAdapter (  private val subscriptionTypes: List<SubscriptionType>,   private val onSubscriptionClick: (SubscriptionType) -> Unit) :
    RecyclerView.Adapter<OffersubsAdapter.OfferViewHolder>() {
    private var selectedItemPosition: Int = -1
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    inner class OfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOffer: TextView = itemView.findViewById(R.id.tvOffer)
        val tvDuration: TextView = itemView.findViewById(R.id.tvDuration)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
             val container: View = itemView.findViewById(R.id.container)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    selectedPosition = position
                    notifyDataSetChanged() // Notify that the data has changed to update the UI
                    val subscriptionType = subscriptionTypes[position]
                    onSubscriptionClick(subscriptionType)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subscription, parent, false)
        return OfferViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val subscriptionType = subscriptionTypes[position]
        holder.tvOffer.text = subscriptionType.title
        holder.tvDuration.text = subscriptionType.description
        holder.tvPrice.text = subscriptionType.price

        // Highlight the selected item
        if (position == selectedPosition) {
            holder.container.setBackgroundColor(Color.parseColor("#D3D3D3")) // Light gray background
        } else {
            holder.container.setBackgroundColor(Color.WHITE) // Default background
        }
        }

    override fun getItemCount(): Int = subscriptionTypes.size
    }


