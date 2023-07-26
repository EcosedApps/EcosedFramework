package io.ecosed.framework.application

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.preference.PreferenceManager
import com.farmerbb.taskbar.lib.Taskbar
import com.google.android.material.color.DynamicColors
import com.idlefish.flutterboost.FlutterBoost
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.style.IOSStyle
import io.ecosed.framework.activity.MainActivity
import io.ecosed.framework.delegate.BoostDelegate
import io.ecosed.framework.services.BillingService
import io.ecosed.framework.services.EcosedService
import io.ecosed.framework.services.LicensingService
import io.ecosed.framework.settings.EcosedSettings
import io.flutter.app.FlutterApplication
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugins.GeneratedPluginRegistrant
import org.lsposed.hiddenapibypass.HiddenApiBypass

class EcosedApplication : FlutterApplication() {

    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        initSP()
        initBoost()
        initTaskbar()
        initDynamicColors()
        initDialogX()
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
//        stopService(Intent(this@EcosedApplication, EcosedService().javaClass))
//        stopService(Intent(this@EcosedApplication, BillingService().javaClass))
//        stopService(Intent(this@EcosedApplication, LicensingService().javaClass))
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("L")
        }
    }

    /**
     * 初始化SharedPreferences
     */
    private fun initSP() {
        // 初始化首选项
        EcosedSettings.initialize(context = this@EcosedApplication)
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@EcosedApplication)
    }

    /**
     * 初始化Flutter Boost
     */
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

    /**
     * 初始化LibTaskbar
     */
    private fun initTaskbar() {
        Taskbar.setEnabled(
            this@EcosedApplication,
            mSharedPreferences.getBoolean(
                "desktop",
                true
            ) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        )
    }

    /**
     * 初始化动态颜色
     */
    private fun initDynamicColors() {
        if (mSharedPreferences.getBoolean(
                "dynamic_colors",
                true
            ) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        ) DynamicColors.applyToActivitiesIfAvailable(this@EcosedApplication)
    }

    /**
     * 初始化Dialog
     */
    private fun initDialogX() {
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
}