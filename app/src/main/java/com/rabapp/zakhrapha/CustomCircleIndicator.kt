package com.rabapp.zakhrapha

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import me.relex.circleindicator.CircleIndicator3

class CustomCircleIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CircleIndicator3(context, attrs, defStyleAttr) {

    private var selectedColor: Int = Color.YELLOW
    private var defaultColor: Int = Color.GRAY
    private var currentPosition: Int = 0

    fun setSelectedColor(color: Int) {
        selectedColor = color
        updateDotColors()
    }

    fun setDefaultColor(color: Int) {
        defaultColor = color
        updateDotColors()
    }

    fun setCurrentPosition(position: Int) {
        currentPosition = position
        updateDotColors()
    }

    private fun updateDotColors() {
        for (i in 0 until childCount) {
            val dot = getChildAt(i)
            dot.setBackgroundColor(
                if (i == currentPosition) selectedColor else defaultColor
            )
        }
    }
}