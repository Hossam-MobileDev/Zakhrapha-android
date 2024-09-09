package com.rabapp.zakhrapha

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
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
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

internal class AsmaaViewAdapter(
    private var itemsList: List<String>,
    private val onFavoriteClicked: (String) -> Unit,
    private val onCopyClicked: (String) -> Unit,
    val context: Context,
    private val favoriteViewModel: HistoryViewModel) :
    RecyclerView.Adapter<AsmaaViewAdapter.MyViewHolder>() {

    private val favoriteItems: MutableSet<String> = mutableSetOf()
    private val gson = Gson()
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
   /* private val database: AppDatabase? = AppDatabase.getDatabase(context)
    private val favDao: FavoriteDao = database!!.favoriteDao()*/
    private val ITEM_TYPE_REGULAR = 0
    private val ITEM_TYPE_NATIVE_AD = 1
    private var nativeAd: NativeAd? = null
    private val PREFS_NAME = "copy_limit_prefs"
    private val COPY_COUNT_KEY = "copy_count"
    private val LAST_RESET_KEY = "last_reset_time"
    private val MAX_COPIES = 5
    private var rewardedAd: RewardedAd? = null
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemTextView: TextView = view.findViewById(R.id.txtrow)
        var copyimage : ImageView = view.findViewById(R.id.copyimg)
        var favimage : ImageView = view.findViewById(R.id.favimage)


    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsmaaViewAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.asmaarow, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.itemTextView.text = item
        holder.copyimage.setOnClickListener {
            onCopyClicked(item)
            // saveToHistory(item)
            //insertIntoHistory(item)
        }
        holder.itemView.setOnClickListener{
            onCopyClicked(item)
        }
        updateFavoriteButton(holder.favimage, item)
        holder.favimage.setOnClickListener {
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

   /* private fun insertIntoFavorite(text: String) {
        // Format the current date
        //val currentDate = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(Date())

        // Use a coroutine to insert the item into the database
        CoroutineScope(Dispatchers.IO).launch {
            val favItem = FavoriteItem( text)
            favDao.insert(favItem)
        }
    }*/
    private fun incrementCopyCount(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val currentCount = prefs.getInt(COPY_COUNT_KEY, 0)
        prefs.edit().putInt(COPY_COUNT_KEY, currentCount + 1).apply()
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
            loadRewardedAd(context) // Load ad before showing it
            dialog.dismiss()
        }
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
*/
    private fun isSameDay(time1: Long, time2: Long): Boolean {
        val calendar1 = Calendar.getInstance().apply { timeInMillis = time1 }
        val calendar2 = Calendar.getInstance().apply { timeInMillis = time2 }
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

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
    private fun handleReward(rewardItem: RewardItem) {
        // Implement reward handling logic here
        println("User earned ${rewardItem.amount} ${rewardItem.type}")
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
