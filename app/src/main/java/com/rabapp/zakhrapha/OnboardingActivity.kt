package com.rabapp.zakhrapha

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import de.hdodenhof.circleimageview.CircleImageView


class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var btnNext: Button
    private lateinit var indicatorLayout: LinearLayout
    private val images = listOf(R.drawable.intro1, R.drawable.intro2, R.drawable.intro3, R.drawable.intro4)
    private val titles = listOf(
        "تصميم الاسم",
        "تحسين النصوص",
        "اضافة رموز",
        "اضافة اشكال"
    )
    private val texts = listOf(
        "الان يمكنك ان تصمم اسمك بشكل جديد يجعلك مميزا بين اصدقاءك و مشاركته علي مواقع التواصل الاجتماعي\n",
        "تحسين شكل و جاذبيه النصوص باشكال و تاثيرات مميزة\n",
        "اضف طابع خاص علي نصوصك من خلال اضافه رموز له لجعله اكثر ابداعا و فراده\n",
        "اضافه الوجوه و الاشكال المبتكرة لخطك لجعله اكثر تفاعلا و ابداعا\n"
    )
    private var currentPage = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
viewPager = findViewById(R.id.viewPager)
        val skiptext = findViewById<TextView>(R.id.skipid)
        skiptext.setOnClickListener{startActivity(Intent(this,HomeActivity::class.java))}

        btnNext = findViewById(R.id.btnNext)
        indicatorLayout = findViewById(R.id.indicatorLayout)
        val adapter = OnboardingPagerAdapter(images , texts,titles)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(pageChangeListener)
        setupIndicators(images.size)
        btnNext.setOnClickListener {
            currentPage++
            if (currentPage < images.size) {
                viewPager.currentItem = currentPage
            } else {
                val preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                preferences.edit().putBoolean("isOnboardingCompleted", true).apply()
                val intent = Intent(this@OnboardingActivity, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                startActivity(intent)
finish()
            }
        }
        btnNext.setBackgroundColor(ContextCompat.getColor(this, R.color.deep_orange_400))
        //finish();

    }
    private fun setIndicator(position: Int) {
        for (i in 0 until indicatorLayout.childCount) {
            val imageView = indicatorLayout.getChildAt(i) as CircleImageView
            imageView.setImageResource(
                if (i == position) R.drawable.circleorange
                else R.drawable.circlelightorange
            )
        }
    }
    private val pageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            currentPage = position
            setIndicator(position)

            btnNext.text = if (position == images.size - 1) "تصفح التطبيق" else "التالي"
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }
    private fun setupIndicators(count: Int) {
        val indicators = Array(count) { CircleImageView(this) }
        for (i in indicators.indices) {
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            indicators[i].layoutParams = params
            indicators[i].setImageResource(R.drawable.circlelightorange)
            indicatorLayout.addView(indicators[i])
        }
        setIndicator(0)
    }
}