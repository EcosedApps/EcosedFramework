package io.ecosed.framework.services

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.blankj.utilcode.util.PermissionUtils
import io.ecosed.framework.BuildConfig
import io.ecosed.framework.EcosedFramework
import io.ecosed.framework.R
import io.ecosed.framework.activity.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import rikka.shizuku.Shizuku

class EcosedService : Service() {

    override fun onCreate() {
        super.onCreate()
        runStartForeground()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        return object : EcosedFramework.Stub() {
            override fun getFrameworkVersion(): String = getFramework()
            override fun getShizukuVersion(): String = getShizuku()
            override fun getSystemVersion(): String = getSystem()
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun openFramework() {
        CoroutineScope(Dispatchers.Main).launch {
            val intent = Intent(this@EcosedService, MainActivity().javaClass)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun getFramework(): String {
        return BuildConfig.VERSION_NAME
    }

    private fun getShizuku(): String {
        return try {
            "Shizuku ${Shizuku.getVersion()}"
        } catch (e: Exception) {
            Log.getStackTraceString(e)
        }
    }

    private fun getSystem(): String {
        return when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.N -> "Android Nougat 7.0"
            Build.VERSION_CODES.N_MR1 -> "Android Nougat 7.1"
            Build.VERSION_CODES.O -> "Android Oreo 8.0"
            Build.VERSION_CODES.O_MR1 -> "Android Oreo 8.1"
            Build.VERSION_CODES.P -> "Android Pie 9"
            Build.VERSION_CODES.Q -> "Android Q 10"
            Build.VERSION_CODES.R -> "Android R 11"
            Build.VERSION_CODES.S -> "Android S 12"
            Build.VERSION_CODES.S_V2 -> "Android Sv2 12.1"
            Build.VERSION_CODES.TIRAMISU -> "Android Tiramisu 13"
            34 -> "Android UpsideDownCake 14"
            else -> "unknown"
        }
    }

    private fun runStartForeground() {
        setupNotificationChannel()
        startForeground(notificationId, buildNotification())
    }

    private fun setupNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        // 创建一个通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, "name", importance).apply {
                description = "descriptionText"
            }
            // 在系统中注册通知渠道
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionUtils.permission(Manifest.permission.POST_NOTIFICATIONS)
        }



        val contentIntent = PendingIntent.getActivities(
            this@EcosedService,
            0,
            arrayOf(
                Intent(
                    this@EcosedService,
                    MainActivity().javaClass
                ).addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                            or Intent.FLAG_ACTIVITY_CLEAR_TOP
                )
            ),
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("应用正在运行")
            .setSmallIcon(R.drawable.baseline_keyboard_command_key_24)
//            .setContentIntent(contentIntent)
            .build()

        notification.flags = Notification.FLAG_ONGOING_EVENT

        return notification
    }

    companion object {
        private const val channelId = "id"
        private const val notificationId = 1
    }
}