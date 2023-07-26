package io.ecosed.framework.settings

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import io.ecosed.framework.utils.EnvironmentUtils.isWatch
import java.util.Locale


object EcosedSettings {

    private const val LANGUAGE = "language"
    private const val NIGHT_MODE = "night_mode"

    private lateinit var mSharedPreferences: SharedPreferences

    fun getPreferences(): SharedPreferences {
        return mSharedPreferences
    }

    fun initialize(context: Context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

}