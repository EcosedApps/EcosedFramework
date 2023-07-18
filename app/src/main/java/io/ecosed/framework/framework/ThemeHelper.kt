package io.ecosed.framework.framework

import android.content.Context
import androidx.annotation.StyleRes;
import android.os.Build
import io.ecosed.framework.R
import rikka.core.util.ResourceUtils

object ThemeHelper {
    private const val THEME_DEFAULT = "DEFAULT"
    private const val THEME_BLACK = "BLACK"


    const val KEY_LIGHT_THEME = "light_theme"
    const val KEY_BLACK_NIGHT_THEME = "black_night_theme"
    const val KEY_USE_SYSTEM_COLOR = "use_system_color"

    fun isBlackNightTheme(context: Context): Boolean {
        return EcosedSettings.getPreferences()
            .getBoolean(KEY_BLACK_NIGHT_THEME, EnvironmentUtils.isWatch(context))
    }

    fun isUsingSystemColor(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                EcosedSettings.getPreferences().getBoolean(KEY_USE_SYSTEM_COLOR, true)
    }

    fun getTheme(context: Context): String {
        return if (isBlackNightTheme(context) && ResourceUtils.isNightMode(context.resources.configuration)) {
            THEME_BLACK
        } else {
            EcosedSettings.getPreferences().getString(KEY_LIGHT_THEME, THEME_DEFAULT)!!
        }
    }

    @StyleRes
    fun getThemeStyleRes(context: Context): Int {
        return when (getTheme(context)) {
            THEME_BLACK -> R.style.ThemeOverlay_Black
            THEME_DEFAULT -> R.style.ThemeOverlay
            else -> R.style.ThemeOverlay
        }
    }
}