package io.ecosed.framework.delegate

import android.content.Intent
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostDelegate
import com.idlefish.flutterboost.FlutterBoostRouteOptions
import com.idlefish.flutterboost.containers.FlutterBoostActivity
import io.ecosed.framework.activity.FlutterActivity
import io.ecosed.framework.activity.MainActivity
import io.flutter.embedding.android.FlutterActivityLaunchConfigs

class BoostDelegate : FlutterBoostDelegate {

    override fun pushNativeRoute(options: FlutterBoostRouteOptions?) {
        val intent = Intent(
            FlutterBoost.instance().currentActivity(),
            MainActivity::class.java
        )
        when (options?.pageName()) {
            "native" -> FlutterBoost.instance().currentActivity()
                .startActivityForResult(intent, options.requestCode())
        }
    }

    override fun pushFlutterRoute(options: FlutterBoostRouteOptions?) {
        val intent =
            FlutterBoostActivity.CachedEngineIntentBuilder(FlutterActivity::class.java)
                .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent)
                .destroyEngineWithActivity(false)
                .uniqueId(options?.uniqueId())
                .url(options?.pageName())
                .urlParams(options?.arguments())
                .build(FlutterBoost.instance().currentActivity())
        FlutterBoost.instance().currentActivity().startActivity(intent)
    }
}