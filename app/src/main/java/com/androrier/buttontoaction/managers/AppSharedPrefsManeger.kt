package com.androrier.buttontoaction.managers

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSharedPrefsManeger @Inject constructor(@ApplicationContext context : Context?){
    private val prefs: SharedPreferences = getDefaultSharedPreferences(context)
    val FIRST_RUN = "First_Run"

    fun cancelFirstAppRun() {
        prefs.edit().putBoolean(FIRST_RUN, false).apply()
    }
    fun getIsFirstAppRun() :Boolean{
        return prefs.getBoolean(FIRST_RUN, true)
    }
}