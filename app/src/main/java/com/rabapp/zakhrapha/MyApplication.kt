package com.rabapp.zakhrapha

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.ads.MobileAds
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
       MobileAds.initialize(this) { }
      //  MobileAds.initialize(this) { initializationStatus -> }

   //     Purchases.configure(this, "sk_kJHbjDRtopAJPaqXopabdaPuHTIJQ")


    }
    }
