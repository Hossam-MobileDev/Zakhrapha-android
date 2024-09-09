package com.rabapp.zakhrapha

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class NonSwipeableViewPagerWrapper @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        // Allow the RecyclerView to handle touch events
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerkalemat)
        if (recyclerView != null && isTouchOnView(recyclerView, ev)) {
            return false
        }
        // Intercept other touch events
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // Return false to prevent touch events from reaching the ViewPager2
        return false
    }

    private fun isTouchOnView(view: View, event: MotionEvent?): Boolean {
        event?.let {
            val rect = Rect()
            view.getGlobalVisibleRect(rect)
            return rect.contains(event.rawX.toInt(), event.rawY.toInt())
        }
        return false
    }
}