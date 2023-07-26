package io.ecosed.framework.application

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.google.android.material.color.DynamicColors
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostDelegate
import com.idlefish.flutterboost.FlutterBoostRouteOptions
import com.idlefish.flutterboost.containers.FlutterBoostActivity
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.style.IOSStyle
import io.ecosed.framework.activity.FlutterActivity
import io.ecosed.framework.activity.MainActivity
import io.ecosed.framework.delegate.BoostDelegate
import io.ecosed.framework.settings.EcosedSettings
import io.flutter.app.FlutterApplication
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugins.GeneratedPluginRegistrant
import org.lsposed.hiddenapibypass.HiddenApiBypass
import rikka.material.app.LocaleDelegate


class EcosedApplication : FlutterApplication() {

    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        initSP()
        initBoost()
        initDialogX()
        initDynamicColors()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("L")
        }
    }

    private fun initSP() {
        // 初始化首选项
        EcosedSettings.initialize(context = this@EcosedApplication)
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@EcosedApplication)
    }

    private fun initBoost() {
        FlutterBoost.instance().setup(
            this@EcosedApplication,
            BoostDelegate()
        ) { engine: FlutterEngine? ->
            engine?.let {
                try {
                    it.plugins.add(MainActivity()).run {
                        GeneratedPluginRegistrant.registerWith(it)
                    }
                } catch (e: Exception) {
                    Log.e(MainActivity.tag, Log.getStackTraceString(e))
                }
            }
        }
    }

    private fun initDialogX() {
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
    }

    private fun initDynamicColors() {
        // 初始化动态颜色
        if (EcosedSettings.getPreferences().getBoolean(
                "dynamic_colors",
                true
            ) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        ) DynamicColors.applyToActivitiesIfAvailable(this@EcosedApplication)
    }


}