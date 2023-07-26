package io.ecosed.framework.activity

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatDelegate
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
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.window.layout.DisplayFeature
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ServiceUtils
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.internal.EdgeToEdgeUtils
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostDelegate
import com.idlefish.flutterboost.FlutterBoostRouteOptions
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.ecosed.framework.R
import io.ecosed.framework.databinding.ContainerBinding
import io.ecosed.framework.fragment.ReturnFragment
import io.ecosed.framework.services.EcosedService
import io.ecosed.framework.ui.layout.ActivityMain
import io.ecosed.framework.ui.theme.EcosedFrameworkTheme
import io.ecosed.framework.utils.AppCompatFlutter
import io.ecosed.framework.utils.ThemeHelper
import io.flutter.embedding.android.FlutterEngineConfigurator
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import rikka.core.ktx.unsafeLazy
import rikka.core.res.isNight
import rikka.material.app.MaterialActivity
import rikka.shizuku.Shizuku
import java.lang.ref.WeakReference


class MainActivity : MaterialActivity(), FlutterBoostDelegate, FlutterBoost.Callback, FlutterPlugin,
    MethodChannel.MethodCallHandler, AppCompatFlutter, FlutterEngineConfigurator,
    Shizuku.OnBinderReceivedListener, Shizuku.OnBinderDeadListener,
    Shizuku.OnRequestPermissionResultListener, DefaultLifecycleObserver, Runnable {

    private lateinit var aidlSer: Intent

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var mActionBar: ActionBar
    private lateinit var mContainer: FragmentContainerView

    private lateinit var mFlutterFragment: FlutterBoostFragment

    private lateinit var mChannel: MethodChannel

    private val poem: ArrayList<String> = arrayListOf(
        "不向焦虑与抑郁投降，这个世界终会有我们存在的地方。",
        "把喜欢的一切留在身边，这便是努力的意义。",
        "治愈、温暖，这就是我们最终幸福的结局。",
        "我有一个梦，也许有一天，灿烂的阳光能照进黑暗森林。",
        "如果必须要失去，那么不如一开始就不曾拥有。",
        "我们的终点就是与幸福同在。",
        "孤独的人不会伤害别人，只会不断地伤害自己罢了。",
        "如果你能记住我的名字，如果你们都能记住我的名字，也许我或者我们，终有一天能自由地生存着。",
        "对于所有生命来说，不会死亡的绝望，是最可怕的审判。",
        "我不曾活着，又何必害怕死亡。"
    )

    private val toolbar: MaterialToolbar by unsafeLazy {
        MaterialToolbar(this@MainActivity)
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

    // material activity begin

    override fun onCreate(savedInstanceState: Bundle?) {
//        WeakReference(application).get()?.apply {
//            FlutterBoost.instance().setup(
//                this@apply,
//                this@MainActivity,
//                this@MainActivity
//            )
//        }
        aidlSer = Intent(this@MainActivity, EcosedService().javaClass)
        startService(aidlSer)


        Toast.makeText(
            this@MainActivity,
            ServiceUtils.isServiceRunning(
                EcosedService().javaClass
            ).toString(),
            Toast.LENGTH_SHORT
        ).show()


        super<MaterialActivity>.onCreate(savedInstanceState)
        // 绑定布局
        val binding = ContainerBinding.inflate(layoutInflater)
        // 获取Fragment容器控件
        val mFragmentContainerView = binding.navHostFragmentContentMain
        // 绑定导航主机
        val navHostFragment = supportFragmentManager.findFragmentById(
            mFragmentContainerView.id
        ) as NavHostFragment
        // 导航控制器
        navController = navHostFragment.navController
        // 应用栏配置
        appBarConfiguration = AppBarConfiguration(
            navGraph = navController.graph
        )
        // 设置操作栏
        setSupportActionBar(toolbar)
        // 将操作栏与导航控制器绑定
        setupActionBarWithNavController(
            navController = navController,
            configuration = appBarConfiguration
        )
        // 获取操作栏
        supportActionBar?.let {
            mActionBar = it
        }
        mContainer = mFragmentContainerView
        // 随机抽取诗句作为子标题
        mActionBar.subtitle = poem[(poem.indices).random()]

        lifecycle.addObserver(this@MainActivity)
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
        )
        if (ThemeHelper.isUsingSystemColor()) if (resources.configuration.isNight()) {
            theme.applyStyle(R.style.ThemeOverlay_DynamicColors_Dark, true)
        } else {
            theme.applyStyle(R.style.ThemeOverlay_DynamicColors_Light, true)
        }.run {
            theme.applyStyle(ThemeHelper.getThemeStyleRes(context = this@MainActivity), true)
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

    // flutter boost delegate begin

    override fun pushNativeRoute(options: FlutterBoostRouteOptions?) {

    }

    override fun pushFlutterRoute(options: FlutterBoostRouteOptions?) {

    }

    // flutter boost delegate end

    // flutter boost start begin

    override fun onStart(engine: FlutterEngine?) {
        engine?.let {
            try {
                it.plugins.add(this@MainActivity).run {
                    GeneratedPluginRegistrant.registerWith(it)
                }
            } catch (e: Exception) {
                Log.e(tag, Log.getStackTraceString(e))
            }
        }
    }

    // flutter boost start end

    // flutter plugin begin

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        mChannel = MethodChannel(binding.binaryMessenger, channel)
        mChannel.setMethodCallHandler(this@MainActivity)
        Log.i(tag, "中中中中中")
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        mChannel.setMethodCallHandler(null)
    }

    // flutter plugin end

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {

        }
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
            .build<FlutterBoostFragment?>().also {
                mFlutterFragment = it
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
                    container = mContainer,
                    flutter = mFlutterFrame,

                    appsUpdate = {},
                    topBarVisible = true,
                    topBarView = toolbar,
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
        const val channel: String = "flutter_ecosed"
    }

}