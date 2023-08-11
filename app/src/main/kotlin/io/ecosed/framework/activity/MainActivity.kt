package io.ecosed.framework.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Resources
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.android.material.internal.EdgeToEdgeUtils
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.ecosed.framework.EcosedFramework
import io.ecosed.framework.R
import io.ecosed.framework.databinding.ContainerBinding
import io.ecosed.framework.delegate.BoostDelegate
import io.ecosed.framework.fragment.ReturnFragment
import io.ecosed.framework.services.EcosedService
import io.ecosed.framework.ui.layout.ActivityMain
import io.ecosed.framework.ui.theme.EcosedFrameworkTheme
import io.ecosed.framework.utils.AppCompatFlutter
import io.ecosed.framework.utils.ThemeHelper
import io.ecosed.plugin.EcosedPlugin
import io.ecosed.plugin.EcosedPluginBinding
import io.ecosed.plugin.EcosedPluginEngine
import io.ecosed.plugin.EcosedPluginMethod
import io.ecosed.plugin.EcosedResult
import io.ecosed.plugin.PluginEngineBuilder
import io.flutter.embedding.android.FlutterEngineConfigurator
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import rikka.core.ktx.unsafeLazy
import rikka.core.res.isNight
import rikka.material.app.MaterialActivity
import rikka.shizuku.Shizuku
import java.lang.ref.WeakReference
import java.util.ServiceLoader

class MainActivity : MaterialActivity(), FlutterBoost.Callback, ServiceConnection, FlutterPlugin,
    EcosedPlugin,
    MethodChannel.MethodCallHandler, ActivityAware, AppCompatFlutter, FlutterEngineConfigurator,
    Shizuku.OnBinderReceivedListener, Shizuku.OnBinderDeadListener,
    Shizuku.OnRequestPermissionResultListener, DefaultLifecycleObserver, Runnable {

    private lateinit var aidlSer: Intent

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var mContainer: FragmentContainerView

    private lateinit var mFlutterFragment: FlutterBoostFragment

    private lateinit var mChannel: MethodChannel

    private var mFramework: EcosedFramework? = null

    private lateinit var mEngine: EcosedPluginEngine
    private var mActivity: Activity? = null
    private lateinit var pluginMethod: EcosedPluginMethod

    private val splashLogo: AppCompatImageView by unsafeLazy {
        AppCompatImageView(this@MainActivity).apply {

        }
    }

    private val mFlutterFrame: FrameLayout by unsafeLazy {
        FrameLayout(this@MainActivity)
    }

    private val mFlutterTips: AppCompatTextView by unsafeLazy {
        AppCompatTextView(this@MainActivity).apply {
            gravity = Gravity.CENTER
            text = "Flutter处于销毁状态！\n请点击右下角按钮后继续"
        }
    }

    override fun onStart(engine: FlutterEngine?) {
        engine?.let {
            try {
                it.plugins.add(MainActivity()).run {
                    GeneratedPluginRegistrant.registerWith(it)
                }
            } catch (e: Exception) {
                Log.e(tag, Log.getStackTraceString(e))
            }
        }
    }

    // material activity begin

    override fun onCreate(savedInstanceState: Bundle?) {
        WeakReference(application).get()?.apply {
            FlutterBoost.instance().setup(
                this@apply,
                BoostDelegate(),
                this@MainActivity
            )
        }


        
        aidlSer = Intent(this@MainActivity, EcosedService().javaClass)
        aidlSer.action = "io.ecosed.framework.action"
        startService(aidlSer)
        bindService(aidlSer, this@MainActivity, Context.BIND_AUTO_CREATE)

        super<MaterialActivity>.onCreate(savedInstanceState).run {
            ContainerBinding.inflate(layoutInflater).container.let {
                navController = (supportFragmentManager.findFragmentById(
                    it.id
                ) as NavHostFragment).navController
                appBarConfiguration = AppBarConfiguration(
                    navGraph = navController.graph
                )
                mContainer = it
            }
        }.run {
            lifecycle.addObserver(this@MainActivity)
        }
    }

    override fun computeUserThemeKey(): String {
        super.computeUserThemeKey()
        return ThemeHelper.getTheme(
            context = this@MainActivity
        ) + ThemeHelper.isUsingSystemColor()
    }

    override fun onApplyUserThemeResource(theme: Resources.Theme, isDecorView: Boolean) {
        super.onApplyUserThemeResource(
            theme = theme,
            isDecorView = isDecorView
        ).run {
            if (ThemeHelper.isUsingSystemColor()) if (resources.configuration.isNight()) {
                theme.applyStyle(R.style.ThemeOverlay_DynamicColors_Dark, true)
            } else {
                theme.applyStyle(R.style.ThemeOverlay_DynamicColors_Light, true)
            }.run {
                theme.applyStyle(ThemeHelper.getThemeStyleRes(context = this@MainActivity), true)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onApplyTranslucentSystemBars() {
        super.onApplyTranslucentSystemBars()
        EdgeToEdgeUtils.applyEdgeToEdge(window, true)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    override fun onPostResume() {
        super.onPostResume()
        try {
            mFlutterFragment.onPostResume()
        } catch (e: Exception) {
            Log.e(localClassName, "Flutter Fragment is not initialized.")
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        try {
            mFlutterFragment.onNewIntent(intent)
        } catch (e: Exception) {
            Log.e(localClassName, "Flutter Fragment is not initialized.")
        }
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith(
            expression = "super.onBackPressed()",
            imports = arrayOf(
                "rikka.material.app.MaterialActivity"
            )
        )
    )

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        super.onBackPressed()
        try {
            mFlutterFragment.onBackPressed()
        } catch (e: Exception) {
            Log.e(localClassName, "Flutter Fragment is not initialized.")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        try {
            mFlutterFragment.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
        } catch (e: Exception) {
            Log.e(localClassName, "Flutter Fragment is not initialized.")
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        try {
            mFlutterFragment.onUserLeaveHint()
        } catch (e: Exception) {
            Log.e(localClassName, "Flutter Fragment is not initialized.")
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        try {
            mFlutterFragment.onTrimMemory(level)
        } catch (e: Exception) {
            Log.e(localClassName, "Flutter Fragment is not initialized.")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> navController.navigate(R.id.nav_settings)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super<MaterialActivity>.onDestroy()
        lifecycle.removeObserver(this@MainActivity)
        stopService(aidlSer)
    }

    // material activity end

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Log.i(tag, "服务端绑定成功")

        mFramework = EcosedFramework.Stub.asInterface(service)

    }

    override fun onServiceDisconnected(name: ComponentName?) {

    }

    override fun onBindingDied(name: ComponentName?) {
        super.onBindingDied(name)
    }

    override fun onNullBinding(name: ComponentName?) {
        super.onNullBinding(name)
    }


    // flutter plugin begin


    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        mEngine = PluginEngineBuilder().init(activity = mActivity).build { engine ->
            mChannel = MethodChannel(binding.binaryMessenger, flutterChannel)
            engine.attach()
            engine.addPlugin(plugin = this@MainActivity)
            mChannel.setMethodCallHandler(this@MainActivity)
            return@build engine
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        mChannel.setMethodCallHandler(null)
        mEngine.removePlugin(plugin = this@MainActivity)
        mEngine.detach()
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        mActivity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        this.onDetachedFromActivity()
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        this.onAttachedToActivity(binding = binding)
    }

    override fun onDetachedFromActivity() {
        mActivity = null
    }

    override fun getEcosedPluginMethod(): EcosedPluginMethod {
        return pluginMethod
    }

    override fun onEcosedAttached(binding: EcosedPluginBinding) {
        pluginMethod = EcosedPluginMethod(
            binding = binding,
            channel = ecosedChannel
        )
        pluginMethod.setMethodCallHandler(
            callBack = this@MainActivity
        )
    }

    override fun onEcosedDetached(binding: EcosedPluginBinding) {
        pluginMethod.setMethodCallHandler(callBack = null)
    }

    override fun onEcosedMethodCall(call: String, result: EcosedResult) {
        when (call) {
            "text" -> result.success("")
            else -> result.notImplemented()
        }
    }

    // flutter plugin end

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        result.success(mEngine.execMethodCall(channel = ecosedChannel, call = call.method))
    }

    override fun onFlutterCreated(flutterView: FlutterView?) {
        (flutterView ?: errorFlutterViewNull()).let { create ->
            (create.parent as ViewGroup).removeView(create)
            if (create.parent != mFlutterFrame) {
                mFlutterFrame.addView(
                    create,
                    FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                )
                if (mFlutterTips.parent == mFlutterFrame) {
                    mFlutterFrame.removeView(mFlutterTips)
                }
            }
        }
    }

    override fun onFlutterDestroy(flutterView: FlutterView?) {
        (flutterView ?: errorFlutterViewNull()).let { destroy ->
            if (destroy.parent == mFlutterFrame) {
                mFlutterFrame.removeView(destroy)
                if (mFlutterTips.parent != mFlutterFrame) {
                    mFlutterFrame.addView(
                        mFlutterTips,
                        FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            Gravity.CENTER
                        )
                    )
                }

            }
        }
    }

    override fun getFlutterFragment(): FlutterBoostFragment {
        return FlutterBoostFragment.CachedEngineFragmentBuilder(
            ReturnFragment::class.java
        )
            .destroyEngineWithFragment(false)
            .renderMode(RenderMode.surface)
            .transparencyMode(TransparencyMode.transparent)
            .shouldAttachEngineToActivity(true)
            .build<FlutterBoostFragment?>().also { fragment ->
                mFlutterFragment = fragment
            }
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {

    }

    override fun cleanUpFlutterEngine(flutterEngine: FlutterEngine) {

    }

    override fun onBinderReceived() {

    }

    override fun onBinderDead() {

    }

    override fun onRequestPermissionResult(requestCode: Int, grantResult: Int) {

    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onCreate(owner)
        setContent {
            val windowSize: WindowSizeClass = calculateWindowSizeClass(activity = this@MainActivity)
            val displayFeatures: List<DisplayFeature> =
                calculateDisplayFeatures(activity = this@MainActivity)

            EcosedFrameworkTheme {
                ActivityMain(
                    windowSize = windowSize,
                    displayFeatures = displayFeatures,
                    subNavController = navController,
                    configuration = appBarConfiguration,
                    container = mContainer,
                    flutter = mFlutterFrame,

                    appsUpdate = {},
                    topBarVisible = true,
                    topBarUpdate = {
                        setSupportActionBar(it)
                    },
                    preferenceUpdate = { preference ->

                    },
                    androidVersion = "13",
                    shizukuVersion = "13",
                    current = {},
                    toggle = {},
                    taskbar = {}
                )
            }
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStart(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onPause(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStop(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onDestroy(owner)
    }

    override fun run() {

    }

    private fun errorFlutterViewNull(): Nothing {
        error("Error: FlutterView is null!")
    }

    companion object {
        const val tag: String = "MainActivity"
        const val flutterChannel: String = "flutter_ecosed"
        const val ecosedChannel: String = "ecosed_ecosed"
    }
}