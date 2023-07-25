package io.ecosed.framework.application

import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.color.DynamicColors
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.style.IOSStyle
import io.ecosed.framework.settings.EcosedSettings
import io.flutter.app.FlutterApplication
import org.lsposed.hiddenapibypass.HiddenApiBypass
import rikka.material.app.LocaleDelegate

class EcosedApplication : FlutterApplication() {

    override fun onCreate() {
        super.onCreate()
        // 初始化首选项
        EcosedSettings.initialize(context = this@EcosedApplication)
        // 语言
        LocaleDelegate.defaultLocale = EcosedSettings.getLocale()
        // 初始化深色模式
        AppCompatDelegate.setDefaultNightMode(EcosedSettings.getNightMode(context = this@EcosedApplication))
        // 初始化DialogX
        DialogX.init(this@EcosedApplication)
        DialogX.globalStyle = IOSStyle()
        DialogX.globalTheme = DialogX.THEME.AUTO
        DialogX.autoShowInputKeyboard = true
        DialogX.onlyOnePopTip = false
        DialogX.cancelable = true
        DialogX.cancelableTipDialog = false
        DialogX.bottomDialogNavbarColor = Color.TRANSPARENT
        DialogX.autoRunOnUIThread = true
        DialogX.useHaptic = true
        // 初始化动态颜色
        if (EcosedSettings.getPreferences().getBoolean(
                "dynamic_colors",
                true
            ) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        ) DynamicColors.applyToActivitiesIfAvailable(this@EcosedApplication)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("L")
        }
    }
}