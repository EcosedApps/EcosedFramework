package io.ecosed.framework.framework

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.core.view.WindowCompat
import com.google.android.material.internal.EdgeToEdgeUtils
import io.ecosed.framework.R
import rikka.core.res.isNight

abstract class AppActivity : EcosedPlugin() {

    override fun computeUserThemeKey(): String {
        return ThemeHelper.getTheme(
            context = this@AppActivity
        ) + ThemeHelper.isUsingSystemColor()
    }

    override fun onApplyUserThemeResource(theme: Resources.Theme, isDecorView: Boolean) {
        if (ThemeHelper.isUsingSystemColor()) if (resources.configuration.isNight()) {
            theme.applyStyle(R.style.ThemeOverlay_DynamicColors_Dark, true)
        } else {
            theme.applyStyle(R.style.ThemeOverlay_DynamicColors_Light, true)
        }.run {
            theme.applyStyle(ThemeHelper.getThemeStyleRes(context = this@AppActivity), true)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onApplyTranslucentSystemBars() {
        super.onApplyTranslucentSystemBars()
        // 启用边到边
        EdgeToEdgeUtils.applyEdgeToEdge(window, true)
        // 设置页面布局边界
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (!super.onSupportNavigateUp()) {
            finish()
        }
        return true
    }
}