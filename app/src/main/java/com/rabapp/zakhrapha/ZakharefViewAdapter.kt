package com.rabapp.zakhrapha

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class ZakharefViewAdapter(
    private var itemsList: List<String>,
    private val onFavoriteClicked: (String) -> Unit,
    private val onCopyClicked: (String) -> Unit,

  val  context: Context ,
    private val favoriteViewModel: HistoryViewModel,// Pass the Fragment's context
  //  private val saveToHistory: (String) -> Unit,// Pass the saveToHistory function
    param: (Any) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val database: AppDatabase? = AppDatabase.getDatabase(context)
   private val historyDao: HistoryDao = database!!.historyDao()
   // private val favDao: FavoriteDao = database!!.favoriteDao()
    private val ITEM_TYPE_REGULAR = 0
    private val ITEM_TYPE_NATIVE_AD = 1
    private var nativeAd: NativeAd? = null
    private val PREFS_NAME = "copy_limit_prefs"
    private val COPY_COUNT_KEY = "copy_count"
    private val LAST_RESET_KEY = "last_reset_time"
    private val MAX_COPIES = 5
    private var rewardedAd: RewardedAd? = null

    init {
       // loadNativeAd()
     //   loadRewardedAd(context)

    }

    private val favoriteItems: MutableSet<String> = mutableSetOf()


    inner class RegularViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemTextView: TextView = view.findViewById(R.id.txtelmodlarow)
        var copyImageitem: ImageView = view.findViewById(R.id.copyIdimage)
        var fovoriteImageitem: ImageView = view.findViewById(R.id.favoriteIdImage)


    }

    @NonNull
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

           val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.zakhrfarow, parent, false)
          return  RegularViewHolder(view)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NativeAdViewHolder) {
            // Bind the native ad to the native ad view
            //bindNativeAd(holder.nativeAdView)
        } else if (holder is RegularViewHolder) {
            // Calculate actual position in itemsList
            val actualPosition = position - (position / 5) // Adjusting for ads


            val item = itemsList[position]

            holder.itemTextView.text = item

            // Copy action
            holder.itemView.setOnClickListener{
                onCopyClicked(item)
                // saveToHistory(item)
                insertIntoHistory(item)
            }
            holder.copyImageitem.setOnClickListener {
                onCopyClicked(item)
                // saveToHistory(item)
                insertIntoHistory(item)
            }
            // Update favorite button and set click listener
            updateFavoriteButton(holder.fovoriteImageitem, item)
            holder.fovoriteImageitem.setOnClickListener {
                toggleFavorite(item)
                onFavoriteClicked(item)
            }


            }

        }


    private fun insertIntoHistory(text: String) {
        // Format the current date
        val currentDate = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(Date())

        // Use a coroutine to insert the item into the database
        CoroutineScope(Dispatchers.IO).launch {
            val historyItem = HistoryItem(textContent = text, date = currentDate)
            historyDao.insert(historyItem)
        }
    }
/*
    private fun showCopyLimitDialog() {
        val builder = AlertDialog.Builder(context)
      //  builder.setTitle("Copy Limit Reached")

        // Inflate custom layout (optional)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog, null)
        builder.setView(dialogView)

        // Find views in the custom layout
        val watchAdButton: Button = dialogView.findViewById(R.id.watchNowButton)
        val cancelButton: Button = dialogView.findViewById(R.id.upgradeToProButton)

        // Set dialog message
      //  messageTextView.text = "You have reached your copy limit for today. Watch an ad to continue copying."

        val dialog = builder.create()

        // Set button actions
        watchAdButton.setOnClickListener {
          //  loadRewardedAd(context) // Load ad before showing it
            dialog.dismiss()
        }
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
*/

/*
    private fun showRewardedAd() {
        rewardedAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Ad was dismissed. Load another ad if needed
                   // loadRewardedAd(context) // Optionally load a new ad
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    // Handle the failure
                    Toast.makeText(context, "Ad failed to show.", Toast.LENGTH_SHORT).show()
                }

                override fun onAdShowedFullScreenContent() {
                    // Ad was shown
                    rewardedAd = null // Clear the ad to prevent re-use
                }
            }

            ad.show(context as Activity) { rewardItem ->
                // Handle the reward here
                handleReward(rewardItem)
            }
        } ?: run {
            // Handle the case when the ad is not loaded
            Toast.makeText(context, "Rewarded ad is not ready yet.", Toast.LENGTH_SHORT).show()
        }
    }
*/



    override fun getItemViewType(position: Int): Int {
        return if ((position + 1) % 5 == 0) { // Show ad every 4 items
            ITEM_TYPE_NATIVE_AD
        } else {
            ITEM_TYPE_REGULAR
        }
    }



    private fun checkIfFavorite(item: String): Boolean {
        return favoriteItems.contains(item)

    }
    fun updateItemsList(newItems: List<String>) {
        itemsList = newItems
        notifyDataSetChanged()
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
        notifyItemChanged(itemsList.indexOf(item)) // Update the changed item in the UI
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






    override fun getItemCount(): Int {
        // Include ads in count
        return itemsList.size
    }

    fun updateData(decoratedTexts: List<String>) {
        itemsList = decoratedTexts
      //  favoriteItems.clear()
        notifyDataSetChanged()

    }

/*
    private fun loadNativeAd() {
        val adLoader = AdLoader.Builder(context, "ca-app-pub-1439642083038769/9473650127"
                ) // Replace with your Ad Unit ID
            .forNativeAd { ad ->
                nativeAd = ad
                notifyDataSetChanged() // Notify adapter to refresh the view
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    nativeAd = null
                }
            })
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }
*/

/*
    private fun bindNativeAd(nativeAdView: NativeAdView) {
        nativeAd?.let { ad ->
            nativeAdView.findViewById<TextView>(R.id.ad_headline).text = ad.headline
            // Bind other ad assets to your NativeAdView
            nativeAdView.setNativeAd(ad)
        }
    }
*/

    class NativeAdViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nativeAdView: NativeAdView = view.findViewById(R.id.native_ad_view)
    }
    private fun isCopyAllowed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val currentCount = prefs.getInt(COPY_COUNT_KEY, 0)
        val lastReset = prefs.getLong(LAST_RESET_KEY, 0)
        val currentTime = System.currentTimeMillis()

        if (!isSameDay(lastReset, currentTime)) {
            prefs.edit()
                .putInt(COPY_COUNT_KEY, 0)
                .putLong(LAST_RESET_KEY, currentTime)
                .apply()
            return true
        }

        return currentCount < MAX_COPIES
    }

    // Function to increment the copy count
    private fun incrementCopyCount(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val currentCount = prefs.getInt(COPY_COUNT_KEY, 0)
        prefs.edit().putInt(COPY_COUNT_KEY, currentCount + 1).apply()
    }

    // Function to reset the copy count
    private fun resetCopyCount(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(COPY_COUNT_KEY, 0).apply()
    }

    // Utility method to check if two timestamps are on the same day
    private fun isSameDay(time1: Long, time2: Long): Boolean {
        val calendar1 = Calendar.getInstance().apply { timeInMillis = time1 }
        val calendar2 = Calendar.getInstance().apply { timeInMillis = time2 }
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }
/*
    private fun showCopyLimitDialog(context: Context) {
        // Create an AlertDialog builder
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Copy Limit Reached")

        // Inflate custom layout (optional)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog, null)
        builder.setView(dialogView)

        // Find views in the custom layout
        val messageTextView: TextView = dialogView.findViewById(R.id.messageTextView)
        val watchAdButton: Button = dialogView.findViewById(R.id.watchAdButton)
        val cancelButton: Button = dialogView.findViewById(R.id.cancelButton)

        // Set dialog message
        messageTextView.text = "You have reached your copy limit for today. Watch an ad to continue copying."
        val dialog = builder.create()
        // Set button actions
        watchAdButton.setOnClickListener {
            // Load and show rewarded ad
            loadRewardedAd(context)
            // Dismiss the dialog
            dialog.dismiss()
        }
        cancelButton.setOnClickListener {
            // Dismiss the dialog
            dialog.dismiss()
        }

        // Create and show the dialog

        dialog.show()
    }
*/

/*
    private fun loadRewardedAd(context: Context) {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            context,
            "ca-app-pub-1439642083038769/6410640575", // Replace with your actual ad unit ID
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    // The ad was loaded successfully
                    rewardedAd = ad
                    // Immediately show the ad if appropriate
                    showRewardedAd()
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Handle the failure
                    rewardedAd = null
                }
            }
        )
    }
*/

   /* private fun handleReward(rewardItem: RewardItem) {
        // Implement reward handling logic here
        println("User earned ${rewardItem.amount} ${rewardItem.type}")
    }
  *//*  private fun insertIntoFavorite(text: String) {
        // Format the current date
        //  val currentDate = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(Date())

        // Use a coroutine to insert the item into the database
        CoroutineScope(Dispatchers.IO).launch {
            val favItem = FavoriteItem( text)
            favDao.insert(favItem)
        }
    }*/

}