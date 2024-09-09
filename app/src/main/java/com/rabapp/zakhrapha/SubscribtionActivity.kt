package com.rabapp.zakhrapha

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.android.billingclient.api.Purchase
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Offering
import com.revenuecat.purchases.Offerings
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.interfaces.PurchaseCallback
import com.revenuecat.purchases.interfaces.ReceiveOfferingsCallback
import com.revenuecat.purchases.models.StoreTransaction


class SubscribtionActivity : AppCompatActivity() {
    private lateinit var textViewTitle: TextView
    private lateinit var textViewDescription: TextView
    private lateinit var circleIndicator: CustomCircleIndicator
    private var fetchedOffering: Offering? = null
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: SubscriptionPagerAdapter

    private var selectedSubscription: SubscriptionType? = null
    private val handler = Handler(Looper.getMainLooper())
    private val updatePageRunnable = object : Runnable {
        override fun run() {
            val currentItem = viewPager.currentItem
            val nextItem = (currentItem + 1) % adapter.itemCount
            viewPager.setCurrentItem(nextItem, true)
            handler.postDelayed(this, 3000) // Change page every 3 seconds
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_subscribtion)
        textViewTitle = findViewById(R.id.textViewTitle)
        textViewDescription = findViewById(R.id.textViewDescription)
         viewPager = findViewById(R.id.viewpagersubscribe)
        val button : Button = findViewById(R.id.btnsubscribe)
        button.setOnClickListener {
            selectedSubscription?.let {
                val selectedPackage = getPackageFromSubscriptionItem(it)
                selectedPackage?.let { packageToPurchase ->
                    purchaseSubscription(packageToPurchase)
                } ?: run {
                    // Handle case when package is null
                    Log.e("Subscription", "Package not found for selected item")
                }
            } ?: run {
                // Handle case when no subscription is selected
                Toast.makeText(this, "Please select a subscription plan", Toast.LENGTH_SHORT).show()
            }
        }
val closeimage : ImageView = findViewById(R.id.closeimage)
        closeimage.setOnClickListener { finish() }
        circleIndicator = findViewById(R.id.circleIndicator) as CustomCircleIndicator

        val data = listOf(
            Subscriptviewpager("تحكم في اشتراكك", "ضمان لاسترجاع نقودك", R.drawable.cuate),
            Subscriptviewpager("VIP دعم", "احصل على الدعم السريع من فريق زخرفه.", R.drawable.imgsubs),
                    Subscriptviewpager("استخدام غير محدود لتأثيرات زخرفه", "استمتع باستخدام غير محدود لتأثيرات زخرفه.", R.drawable.cuatetaserat),
        Subscriptviewpager("حجب الاعلانات", "انشيء قصتك على مواقع التواصل لاجتماعي بدون اعلانات.", R.drawable.cuatetwasl)
        )
         adapter = SubscriptionPagerAdapter(this, data)
        viewPager.adapter = adapter
        circleIndicator.setViewPager(viewPager)

        circleIndicator.setSelectedColor(Color.parseColor("#ff804a"))  // Change as needed
        circleIndicator.setDefaultColor(Color.GRAY)
        val recyclerView: RecyclerView = findViewById(R.id.recyclersubscribe)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val offerList = listOf(
            SubscriptionType("Monthly Plan", "Access for 1 month", "$29.99", SubscriptionTypeEnum.MONTHLY),
            SubscriptionType("Annual Plan", "Access for 12 months", "$99.99", SubscriptionTypeEnum.ANNUAL),
            SubscriptionType("Lifetime Plan", "One-time payment", "$199.99", SubscriptionTypeEnum.LIFETIME)
        )
        val adapterrecycle = OffersubsAdapter(offerList) { selectedPlan ->
            // Handle plan selection
            val selectedPackage = getPackageFromSubscriptionItem(selectedPlan)
            selectedPackage?.let {
                purchaseSubscription(it)
            }
        }
        recyclerView.adapter = adapterrecycle
/*
        val offerList = listOf(
            SubscriptionType("اوفر اشتراك", "1 سنة", "99.99 L.E"),
            SubscriptionType("اوفر اشتراك", "6 أشهر", "49.99 L.E"),
            SubscriptionType("اوفر اشتراك", "3 أشهر", "29.99 L.E")
        )*/

       /* val adapterrecycler = OffersubsAdapter(offerList){ item ->
           // onSubscriptionItemClick(item)
        }*/
       // recyclerView.adapter = adapterrecycler

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val currentItem = data[position]
                textViewTitle.text = currentItem.title
                textViewDescription.text = currentItem.description
                circleIndicator.setCurrentPosition(position)            }
        })
        val firstItem = data.first()
        textViewTitle.text = firstItem.title
        textViewDescription.text = firstItem.description
       // circleImageView.setImageResource(firstItem.circleImageResId)
       // val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation)
      //  circleIndicator.startAnimation(scaleAnimation)

        fetchOfferings()
        handler.post(updatePageRunnable)

    }
    override fun onBackPressed() {
        super.onBackPressed()
        // Close the app when back button is pressed
        finish()
        // Optionally, add an animation for when the app closes
        overridePendingTransition(0, 0)
    }
    private fun onSubscriptionItemClick(item: SubscriptionType) {
        val selectedPackage = getPackageFromSubscriptionItem(item)

        selectedPackage?.let {
            purchaseSubscription(it)
        } ?: run {
            // Handle case when package is null (e.g., show an error or log it)
            Log.e("Subscription", "Package not found for selected item")
        }
    }

    private fun getPackageFromSubscriptionItem(item: SubscriptionType): com.revenuecat.purchases.Package? {
        // Map the subscription type to the corresponding RevenueCat Package
        return when (item.type) {
            SubscriptionTypeEnum.MONTHLY -> fetchedOffering?.monthly
            SubscriptionTypeEnum.ANNUAL -> fetchedOffering?.annual
            SubscriptionTypeEnum.LIFETIME -> fetchedOffering?.lifetime
            else -> null
        }
    }

    // Store the fetched offering to use in other functions



    private fun fetchOfferings() {
        Purchases.sharedInstance.getOfferings(object : ReceiveOfferingsCallback {
            override fun onReceived(offerings: Offerings) {
                fetchedOffering = offerings.current
                // Update the UI with fetched offerings if needed
            }

            override fun onError(error: PurchasesError) {
                Log.e("RevenueCat", "Error fetching offerings: ${error.message}")
            }
        })
    }

    private fun displaySubscriptionOptions(subscriptionPackage: com.revenuecat.purchases.Package) {
        // Implement logic to update RecyclerView or other UI elements with the subscription options
        // Example: Update a RecyclerView Adapter with the available packages
    }

    private fun purchaseSubscription(subscriptionPackage: com.revenuecat.purchases.Package) {
        Purchases.sharedInstance.purchasePackage(
            this,  // 'this' refers to the Activity
            subscriptionPackage,
            object : PurchaseCallback {
                override fun onCompleted(storeTransaction: StoreTransaction, customerInfo: CustomerInfo) {
                    // Handle successful purchase
                    handleSuccessfulPurchase(storeTransaction, customerInfo)
                }

                override fun onError(error: PurchasesError, userCancelled: Boolean) {
                    // Handle purchase error
                    Log.e("RevenueCat", "Purchase error: ${error.message}")
                }
            }
        )
    }
    private fun handleSuccessfulPurchase(storeTransaction: StoreTransaction, customerInfo: CustomerInfo) {
        // Example: Unlock premium features or update subscription status
        if (customerInfo.entitlements["premium"]?.isActive == true) {
            // Grant access to premium content
            Log.d("Subscription", "User has premium access")
        }

        // Notify user of successful purchase
        Toast.makeText(this, "Purchase successful for ${storeTransaction.skus.firstOrNull()}", Toast.LENGTH_LONG).show()

        // Optionally, send purchase info to backend for validation or analytics
        //sendPurchaseToServer(storeTransaction, customerInfo)
    }
}



