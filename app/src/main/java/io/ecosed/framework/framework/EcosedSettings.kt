package io.ecosed.framework.framework

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager


object EcosedSettings {

    private lateinit var mSharedPreferences: SharedPreferences

    fun getPreferences(): SharedPreferences{
        return mSharedPreferences
    }

    fun initialize(context: Context){
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }
}