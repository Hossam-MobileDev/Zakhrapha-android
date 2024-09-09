package com.rabapp.zakhrapha

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
internal class KalematwAsharDetailAdapter(
    private val items: List<String>,
    private val onFavoriteClicked: (String) -> Unit,
    private val onCopyClicked: (String) -> Unit,
    requireContext: Context,
    private val favoriteViewModel: HistoryViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val favoriteItems: MutableSet<String> = mutableSetOf()
    private val sharedPreferences: SharedPreferences = requireContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_AD = 1

    private val adLoader: AdLoader = AdLoader.Builder(requireContext, "ca-app-pub-1439642083038769/9473650127")
        .forNativeAd { nativeAd: NativeAd ->
            // No need to do anything here; ads are loaded in onBindViewHolder
        }
        .withNativeAdOptions(NativeAdOptions.Builder().build())
        .build()

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iconFavorite: ImageView = itemView.findViewById(R.id.favoriteIdImage)
        var textAsk: TextView = itemView.findViewById(R.id.txtelmodlarow)
        var iconCopy: ImageView = itemView.findViewById(R.id.copyIdimage)
    }

    internal inner class AdViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val adView: NativeAdView = view.findViewById(R.id.native_ad_view)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_AD) {
            val adView = LayoutInflater.from(parent.context).inflate(R.layout.native_ad, parent, false)
            AdViewHolder(adView)
        } else {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.kalematwashaarrow, parent, false)
            MyViewHolder(itemView)
        }
    }

    override fun getItemCount(): Int {
        // +1 for the ad
        return items.size + (items.size / 4)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_AD) {
            // Load and display the ad here
            loadNativeAd(holder as AdViewHolder)
        } else {
            val adjustedPosition = getAdjustedPosition(position)
            val item = items[adjustedPosition]
            val itemHolder = holder as MyViewHolder
            itemHolder.textAsk.text = item

            itemHolder.iconCopy.setOnClickListener {
                onCopyClicked(item)
            }
            holder.itemView.setOnClickListener{
                onCopyClicked(item)
            }
            updateFavoriteButton(itemHolder.iconFavorite, item)

            itemHolder.iconFavorite.setOnClickListener {
                toggleFavorite(item)
                onFavoriteClicked(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 5 == 4) VIEW_TYPE_AD else VIEW_TYPE_ITEM
    }

    private fun getAdjustedPosition(position: Int): Int {
        return position - (position / 5)
    }

    private fun updateFavoriteButton(favoriteImageItem: ImageView, item: String) {
        val isFavorite = favoriteItems.contains(item)
        favoriteImageItem.setImageResource(
            if (isFavorite) R.drawable.heartred else R.drawable.group_26
        )
    }

    fun toggleFavorite(item: String) {
        if (favoriteItems.contains(item)) {
            favoriteItems.remove(item)
            removeFavoriteFromDatabase(item)
        } else {
            favoriteItems.add(item)
            insertIntoFavorite(item)
        }
        notifyItemChanged(items.indexOf(item))
    }

    private fun removeFavoriteFromDatabase(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val favItem = FavoriteItem(text = text)
            favoriteViewModel.deleteFavoriteItem(favItem)
        }
    }

    private fun insertIntoFavorite(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val favItem = FavoriteItem(text = text)
            favoriteViewModel.insertFavoriteItem(favItem)
        }
    }

    private fun loadNativeAd(holder: AdViewHolder) {
        val adLoader = AdLoader.Builder(holder.itemView.context, "ca-app-pub-1439642083038769/9473650127")
            .forNativeAd { nativeAd: NativeAd ->
                // Populate the NativeAdView with the ad content
                populateNativeAdView(nativeAd, holder.adView)
            }
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        // Ensure native_ad.xml has the necessary views
        adView.findViewById<TextView>(R.id.ad_headline).text = nativeAd.headline
        adView.findViewById<TextView>(R.id.ad_body).text = nativeAd.body
        adView.findViewById<ImageView>(R.id.ad_image).setImageDrawable(nativeAd.images[0].drawable)
        adView.findViewById<Button>(R.id.ad_call_to_action).text = nativeAd.callToAction

        // Register the NativeAdView with the native ad
        adView.setNativeAd(nativeAd)
    }
}