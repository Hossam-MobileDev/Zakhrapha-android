package com.rabapp.zakhrapha

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


private const val PREFS_NAME = "app_prefs"
private const val RETURN_COUNT_KEY = "return_count"
class MainActivity : AppCompatActivity() {
  //  private var interstitialAd: InterstitialAd? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
      //  MobileAds.initialize(this)

        // Load the interstitial ad
       // loadInterstitialAd()

        // Update the return count
      //  updateReturnCount(this)

        // Delay transition to main activity


        val preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val isOnboardingCompleted = preferences.getBoolean("isOnboardingCompleted", false)
        if (!isOnboardingCompleted) {
            Handler(Looper.getMainLooper()).postDelayed({
                // Navigate to OnboardingActivity after a delay
                val intent = Intent(this, OnboardingActivity::class.java)
                // Clear the current task and start a new one
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                // Finish SplashActivity so it cannot be returned to
                finish()
            }, 500)

        } else {
            // Navigate directly to HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            // Clear the current task and start a new one
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            // Finish SplashActivity so it cannot be returned to
            finish()
        }


    }

   /* private fun shouldShowReturnAd(context: Context): Boolean {
        val returnCount = getReturnCount(context)
        return returnCount >= 5
    }*/

   /* private fun getReturnCount(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(RETURN_COUNT_KEY, 0)
    }*/

  /*  private fun updateReturnCount(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val returnCount = sharedPreferences.getInt(RETURN_COUNT_KEY, 0)
        val editor = sharedPreferences.edit()
        editor.putInt(RETURN_COUNT_KEY, returnCount + 1)
        editor.apply()
    }*/




/*
    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, "ca-app-pub-1439642083038769/2356781271"
            , adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    // The ad was successfully loaded
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    // Handle the error
                    interstitialAd = null
                }
            })
    }
*/
/*
    private fun showInterstitialAdOrProceed() {
        if (interstitialAd != null) {
            interstitialAd?.show(this)
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    // Proceed to the main activity after the ad is dismissed
                    navigateToMainActivity()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    super.onAdFailedToShowFullScreenContent(adError)
                    // Proceed to the main activity if the ad fails to show
                    navigateToMainActivity()
                }
            }
        } else {
            // Proceed to the main activity if the ad is not loaded
            startActivity(Intent(this,OnboardingActivity::class.java))
        }
    }
*/
   /* private fun navigateToMainActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // Close the splash activity
    }*/
}



