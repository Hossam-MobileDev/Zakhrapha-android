package com.rabapp.zakhrapha

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class HomeActivity : AppCompatActivity() {

   // private var interstitialAd: InterstitialAd? = null

    //  private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var adView: AdView
    private lateinit var interstitialAd: InterstitialAd
   // private val AD_UNIT_ID = "ca-app-pub-1439642083038769/2356781271"
    private val TEST_AD_UNIT_ID = "ca-app-pub-1439642083038769/2356781271"
    private val APP_OPEN_COUNT_KEY = "app_open_count_key"
    private lateinit var viewPager2: ViewPager2
    private lateinit var maincategoryadapter: MainCategoryAdapter
    private lateinit var zakhareflist: ArrayList<HomeRecyler>
    private lateinit var historyAdapter: HistoryAdapter // Define globally


    // private val itemsList = ArrayList<String>()
    private val asmmalist = ArrayList<String>()

    lateinit var bottomSheetFragment: SettingsFragment
    private val fragments = listOf(
        ZakhrfatFragment(),
        AsmaaFragment(),
        KalematandPoemsFragment(),
        AshkalFragment(),
        ElmofdlaFragment()

    )
    private var currentFragmentIndex = 0 // Index to keep track of the current fragment

    override fun onCreate(savedInstanceState: Bundle?) {
       /* if (ThemeManager.isDarkThemeEnabled(this)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }*/
        super.onCreate(savedInstanceState)

       setContentView(R.layout.activity_home)
       MobileAds.initialize(this)
        adView = findViewById(R.id.adView)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        val sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val appOpenCount = sharedPreferences.getInt(APP_OPEN_COUNT_KEY, 0)

        // Increment and save the app open count
        val newAppOpenCount = appOpenCount + 1
        sharedPreferences.edit().putInt(APP_OPEN_COUNT_KEY, newAppOpenCount).apply()

        // Check if the app has been opened twice
        if (newAppOpenCount == 2) {
            // Load and show the InterstitialAd
            loadInterstitialAd()
        }
val imagetag : ImageView = findViewById(R.id.tagimg)
        imagetag.setOnClickListener {
            startActivity(Intent(this,SubscribtionActivity::class.java))
        }
        val imageView: ImageView = findViewById(R.id.setting)
        viewPager2 = findViewById(R.id.viewPager)
        //val wrapper: NonSwipeableViewPagerWrapper = findViewById(R.id.viewpager_wrapper)
     //   wrapper.setViewPager(viewPager2)
        val viewpagerAdapter = MainCategoryViewpagerAdapter(this, fragments)
        viewPager2.adapter = viewpagerAdapter

        /*
                viewPager2.isUserInputEnabled = false
                viewPager2.setPageTransformer { page, position ->
                    page.alpha = 1f
                    page.translationX = 0f
                    page.translationY = 0f
                    page.scaleX = 1f
                    page.scaleY = 1f
                }*/
        // viewPager2.setCurrentItem(1,false)
        val recyclerViewHome: RecyclerView = findViewById(R.id.recyclerviewhome1)
        //  val recyclerZakharef: RecyclerView = findViewById(R.id.recyclerzakharef)
        /*fragments.forEach { fragment ->
            (fragment as? ViewModelOwner)?.setViewModel(sharedViewModel)*/

        val elsegeltext: TextView = findViewById<TextView>(R.id.elsegeltxt)
        elsegeltext.setOnClickListener {
            showHistoryBottomSheet()
        }
        imageView.setOnClickListener {
            bottomSheetFragment = SettingsFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
        zakhareflist = ArrayList()
        maincategoryadapter = MainCategoryAdapter(zakhareflist) { position ->
            viewPager2.currentItem = position
        }

        recyclerViewHome.adapter = maincategoryadapter
        /*val
        zakarefAdapter = ZakharefViewAdapter(itemsList) { index ->
            selectedDecorationIndex = index
            //   val currentText = inputTextField.text.toString()
           // val decoratedText = applyDecoration(currentText, selectedDecorationIndex)
            // outputLabel.text = decoratedText
        }
*/
        //  recyclerZakharef.adapter = zakarefAdapter
        adddatatolist()
        viewPager2.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    maincategoryadapter.setSelectedPosition(position)
                   // recyclerViewHome.smoothScrollToPosition(position)
                }
            }


        )
        // prepareItems()
        // asmmaItems()
    }
    override fun onPause() {
        super.onPause()
        adView.pause()
    }

    override fun onResume() {
        super.onResume()
        adView.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        adView.destroy()
    }
    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this, TEST_AD_UNIT_ID, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                interstitialAd = ad
                showInterstitialAd()
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Handle ad load failure
                Log.d("TAG", "Ad failed to load: ${adError.message}")
            }
        })
    }
    private fun showInterstitialAd() {
        if (::interstitialAd.isInitialized) {
            interstitialAd.show(this)
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }

    private fun adddatatolist() {
        zakhareflist.add(HomeRecyler("زخرفات", R.drawable.close, R.drawable.backgzakhrfat))
        zakhareflist.add(HomeRecyler("أسماء", R.drawable.vectoruserprofile, R.drawable.backgasmaa))
        zakhareflist.add(HomeRecyler("كلمات واشعار", R.drawable.kalemat, R.drawable.backgkalemat))

        zakhareflist.add(HomeRecyler("أشكال", R.drawable.shapes, R.drawable.backgashkal))
        zakhareflist.add(HomeRecyler("المفضلة", R.drawable.heartelmofdla, R.drawable.backgmofadla))

        //   zakhareflist.add(HomeRecyler("تخصيص",R.drawable.keysvector,R.drawable.backgtakhses))

    }

    private fun handlehideClick(bottomsheetdialog: BottomSheetDialog) {
        bottomsheetdialog.dismiss()

    }



    private fun showHistoryBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val dialogView: View = layoutInflater.inflate(R.layout.dialog_history, null)
        bottomSheetDialog.setContentView(dialogView)

        // Setup RecyclerView
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        historyAdapter = HistoryAdapter(
            historyItems = mutableListOf(),
            onCopyClicked = { text -> handleCopyClick(text) },
            onHideClick = { position -> handleRemoveItem(position) }
        )
        recyclerView.adapter = historyAdapter

        // Observe ViewModel and update adapter
        historyViewModel.allHistoryItems?.observe(this, { historyItems ->
           historyAdapter.updateItems(historyItems.toMutableList())
        })

        // Setup BottomSheet behavior
        bottomSheetDialog.setOnShowListener {
            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                val behavior = BottomSheetBehavior.from(sheet)

                // Set specific height
                val desiredHeight = 1200 // Replace with desired height
                val layoutParams = sheet.layoutParams
                layoutParams.height = desiredHeight
                sheet.layoutParams = layoutParams

                // Set peek height and initial state
                behavior.peekHeight = desiredHeight
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.isHideable = false
                behavior.isDraggable = false
            }
        }

        // Show BottomSheetDialog
        bottomSheetDialog.show()
    }

    private fun handleCopyClick(text: String) {
        if (CopyCountManager.updateCopyCount(this)) {
            // Copy text to clipboard
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", text)
            clipboard.setPrimaryClip(clip)

            // Get remaining copy count and show a custom toast
            val remainingCount = CopyCountManager.getCopyCount(this)
            DialogUtils.showCustomToast(this, "تم النسخ متبقي لديك: $remainingCount")

            // Show dialog if copy limit is reached
            if (remainingCount == 0) {
                DialogUtils.showCopyLimitDialog(this, text)
            }
        } else {
            // Show dialog when copy limit is reached
            DialogUtils.showCopyLimitDialog(this, text)
        }
    }

    private fun handleRemoveItem(position: Int) {
        historyAdapter.removeItem(position)

    }


    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.history_options_menu) // Inflate the menu resource

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_delete_all -> {
                    // Handle the delete all action
                    deleteAllHistoryItems()
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
    }

    private fun deleteAllHistoryItems() {
        // Example: Deleting all items from ViewModel or data source
        val historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        historyViewModel.deleteAllItems()
    }
}