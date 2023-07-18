package io.ecosed.framework.framework

import android.app.Application
import android.os.Bundle
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostDelegate
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodChannel
import rikka.material.app.MaterialActivity

abstract class BaseClass : MaterialActivity(), FlutterBoostDelegate, FlutterBoost.Callback,
    FlutterPlugin, MethodChannel.MethodCallHandler {

    private lateinit var mApplication: Application

    override fun onCreate(savedInstanceState: Bundle?) {
        mApplication = application
        initFlutterBoost(application = mApplication)
        super.onCreate(savedInstanceState)
    }

    abstract fun initFlutterBoost(application: Application)
}