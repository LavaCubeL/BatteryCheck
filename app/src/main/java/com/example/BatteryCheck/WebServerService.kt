package com.example.BatteryCheck


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class WebServerService : Service() {
    private var myWebServer: MyWebServer? = null

    override fun onCreate() {
        super.onCreate()
        // Starts the server
        myWebServer = MyWebServer(this, 8080).also { it.start() }
        // makes it a service in the foreground
        startForegroundNotification()
    }

    override fun onDestroy() {
        myWebServer?.stop()
        myWebServer = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {

        return null
    }

    private fun startForegroundNotification() {
        val channelId = "web_server_channel"
        val channelName = "Web Server"

        // Creates the notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        // Builds the notification
        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setContentTitle("Web Server is Running")
            .setContentText("Server is running on port 8080")
            .setOngoing(true)
            .build()

        // Starts the service in the foreground
        startForeground(1, notification)
    }
}
