package com.ultra.game.space.managers

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {
    val prefs: SharedPreferences = context.getSharedPreferences("GameSpaceSettings", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String = prefs.getString(key, defValue) ?: defValue
    fun putString(key: String, value: String) = prefs.edit().putString(key, value).apply()

    fun getBoolean(key: String, defValue: Boolean): Boolean = prefs.getBoolean(key, defValue)
    fun putBoolean(key: String, value: Boolean) = prefs.edit().putBoolean(key, value).apply()
    
    fun getFloat(key: String, defValue: Float): Float = prefs.getFloat(key, defValue)
    fun putFloat(key: String, value: Float) = prefs.edit().putFloat(key, value).apply()
}
