package com.rabapp.zakhrapha

import android.content.Context

object ThemeManager {
    private const val PREFS_NAME = "theme_prefs"
    private const val THEME_KEY = "theme_key"

    fun isDarkThemeEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(THEME_KEY, false) // Default to light theme
    }

    fun setDarkThemeEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putBoolean(THEME_KEY, enabled)
            apply()
        }
    }
}