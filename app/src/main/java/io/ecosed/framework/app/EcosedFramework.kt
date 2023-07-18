package io.ecosed.framework.app

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.os.Build
import com.google.android.material.color.DynamicColors
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogxmaterialyou.style.MaterialYouStyle
import io.ecosed.framework.framework.EcosedSettings
import io.flutter.app.FlutterApplication
import org.lsposed.hiddenapibypass.HiddenApiBypass

class EcosedFramework : FlutterApplication() {

    override fun onCreate() {
        super.onCreate()
        EcosedSettings.initialize(context = this@EcosedFramework)
        // 初始化DialogX
        DialogX.init(this@EcosedFramework)
        DialogX.globalStyle = MaterialYouStyle()
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
        ) DynamicColors.applyToActivitiesIfAvailable(this@EcosedFramework)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("L")
        }
    }
}