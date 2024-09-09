package com.rabapp.zakhrapha

import android.content.Context
import android.content.SharedPreferences
import java.util.Calendar

object CopyCountManager {

    private const val PREFS_NAME = "CopyPrefs"
    private const val KEY_COPY_COUNT = "copy_count"
    private const val KEY_LAST_RESET_DATE = "last_reset_date"
    private const val INITIAL_FREE_COPIES = 5
    private const val SINGLE_COPY_LIMIT = 1

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        return "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)}"
    }

    // Check and reset copy count for the new day (or keep the same if it's the same day)
    private fun resetIfNewDay(context: Context) {
        val prefs = getPreferences(context)
        val lastResetDate = prefs.getString(KEY_LAST_RESET_DATE, "")
        val currentDate = getCurrentDate()

        if (lastResetDate != currentDate) {
            // It's a new day; reset the copy count
            prefs.edit()
                .putInt(KEY_COPY_COUNT, INITIAL_FREE_COPIES)
                .putString(KEY_LAST_RESET_DATE, currentDate)
                .apply()
        }
    }

    // Get the current copy count
    fun getCopyCount(context: Context): Int {
        resetIfNewDay(context)
        val prefs = getPreferences(context)
        return prefs.getInt(KEY_COPY_COUNT, INITIAL_FREE_COPIES)
    }

    // Decrease the copy count after each successful copy
    fun updateCopyCount(context: Context): Boolean {
        resetIfNewDay(context)
        val prefs = getPreferences(context)
        val currentCount = prefs.getInt(KEY_COPY_COUNT, INITIAL_FREE_COPIES)

        return if (currentCount > 0) {
            // Update the copy count
            prefs.edit().putInt(KEY_COPY_COUNT, currentCount - 1).apply()
            true
        } else {
            // No copies left
            false
        }
    }

    // Allow one extra copy after watching the rewarded ad
    fun allowNextCopy(context: Context) {
        val prefs = getPreferences(context)
        prefs.edit().putInt(KEY_COPY_COUNT, SINGLE_COPY_LIMIT).apply()
    }
}