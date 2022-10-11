package com.example.besinlerkitabi.util

import android.content.Context
import android.preference.PreferenceManager
import androidx.core.content.edit


class SharedPreferences {

    companion object {
        private val ZAMAN = "zaman"
        private var sharedPreferences: android.content.SharedPreferences? = null

        @Volatile
        private var instance: SharedPreferences? = null
        private val lock = Any()

        operator fun invoke(context: Context): SharedPreferences = instance ?: synchronized(lock) {

            instance ?: sharedPreferencesYap(context).also {
                instance = it
            }
        }

        private fun sharedPreferencesYap(context: Context): SharedPreferences {

            sharedPreferences =
                androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPreferences()
        }
    }

    fun zamaniKaydet(zaman: Long){
        sharedPreferences?.edit(commit = true){
            putLong(ZAMAN,zaman)
        }
    }

    fun zamaniAl() = sharedPreferences?.getLong(ZAMAN,0)
}