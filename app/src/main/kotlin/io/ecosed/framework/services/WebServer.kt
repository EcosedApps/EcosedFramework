package io.ecosed.framework.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.yanzhenjie.andserver.AndServer
import com.yanzhenjie.andserver.Server
import java.util.concurrent.TimeUnit

class WebServer : Service() {

    private lateinit var server: Server

    override fun onCreate() {
        super.onCreate()
        server = AndServer.webServer(this@WebServer)
            .port(8080)
            .timeout(10, TimeUnit.SECONDS)
            .build()
        server.startup()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        server.shutdown()
    }
}