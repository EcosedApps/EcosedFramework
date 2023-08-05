package io.ecosed.framework.utils

import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.flutter.embedding.android.FlutterView

interface AppCompatFlutter {

    fun onFlutterCreated(flutterView: FlutterView?)
    fun onFlutterDestroy(flutterView: FlutterView?)
    fun getFlutterFragment(): FlutterBoostFragment


}