package com.rabapp.zakhrapha

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter

internal class OnboardingPagerAdapter(private val images: List<Int>,
                                      private val titles: List<String>,

                                      private val texts: List<String>) :
    PagerAdapter() {
    override fun getCount(): Int {
      return  images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_onboarding, container, false)

        val imageView: ImageView = view.findViewById(R.id.imageOnboarding)
        val textView: TextView = view.findViewById(R.id.textOnboarding)
        val titleView = view.findViewById<TextView>(R.id.titleView)
        imageView.setImageResource(images[position])
        textView.text = texts[position]
        titleView.text = titles[position] // Set the title


        container.addView(view)
        return view
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}