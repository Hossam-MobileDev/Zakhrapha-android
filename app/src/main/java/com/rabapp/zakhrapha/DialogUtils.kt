package com.rabapp.zakhrapha

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback


object DialogUtils {
    private var rewardedAd: RewardedAd? = null
    private var pendingTextToCopy: String? = null

    fun showCopyLimitDialog(context: Context, textToCopy: String) {
        pendingTextToCopy = textToCopy // Store the text to copy after ad

        // Inflate the custom alert dialog layout
        val dialogView = LayoutInflater.from(context).inflate(R.layout.custom_alert, null)

        // Create the AlertDialog and set the custom view
        val alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false) // Prevent dismissal on back press or touch outside
            .create()

        val upgradeAdButton = dialogView.findViewById<Button>(R.id.upgrade_button)
upgradeAdButton.setOnClickListener {  val intent = Intent(context, SubscribtionActivity::class.java) // Replace TargetActivity with your activity class
    // Optionally, pass data to the next activity using extras
   // intent.putExtra("someKey", "someValue")

    // Start the activity
    context?.startActivity(intent) }
        // Watch ad button handling
        val watchAdButton = dialogView.findViewById<Button>(R.id.watch_ad_button)
        watchAdButton.setOnClickListener {
            showRewardedAd(context, alertDialog) // Trigger ad and dismiss the dialog on completion
        }

        // Close button handling
        val closeButton = dialogView.findViewById<ImageView>(R.id.close_button)
        closeButton.setOnClickListener {
            alertDialog.dismiss() // Close the dialog when close button is clicked
        }


        // Show the dialog
        alertDialog.show()




        // Load the ad (should ideally be loaded earlier, before showing the dialog)
        loadRewardedAd(context)

    }


    private fun showRewardedAd(context: Context, alertDialog: AlertDialog) {
        rewardedAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Perform copy action after ad is closed
                    alertDialog.dismiss() // Dismiss the alert dialog
                    pendingTextToCopy?.let {
                        copyText(context, it) // Copy the text
                    }
                    showCustomToast(context, "تم النسخ")
                    rewardedAd = null // Clear the ad
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Toast.makeText(context, "Ad failed to show: ${adError.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onAdShowedFullScreenContent() {
                    rewardedAd = null // Clear the ad to prevent re-use
                }
            }

            ad.show(context as Activity) { rewardItem ->
                // Handle reward if needed, but not required here as per your instructions
            }
        } ?: run {
            Toast.makeText(context, "Rewarded ad is not ready yet.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadRewardedAd(context: Context) {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(context, "ca-app-pub-1439642083038769/6410640575", adRequest, object : RewardedAdLoadCallback() {
            override fun onAdLoaded(ad: RewardedAd) {
                rewardedAd = ad
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Toast.makeText(context, "Failed to load rewarded ad: ${adError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun copyText(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
    }

    fun showCustomToast(context: Context, message: String) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.custom_toast, null)

        val toastText = layout.findViewById<TextView>(R.id.toast_text)
        toastText.text = message

        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.setGravity(Gravity.TOP, 0, 100)
        toast.show()
    }
}