
package com.example.BatteryCheck

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1) Request POST_NOTIFICATIONS permission if on Android 13+ (API 33)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }

        // 2) Attach the basic UI layout
        setContentView(R.layout.activity_main)

        // 3) Get references to UI elements
        val serverStatusText = findViewById<TextView>(R.id.serverStatusText)
        val startServerButton = findViewById<Button>(R.id.startServerButton)
        val stopServerButton = findViewById<Button>(R.id.stopServerButton)

        // 4) Start the foreground service
        startServerButton.setOnClickListener {
            val intent = Intent(this, WebServerService::class.java)
            startService(intent)  // Tells the system to start the WebServerService
            serverStatusText.text = "Foreground service started on port 8080"
        }

        // 5) Stop the foreground service
        stopServerButton.setOnClickListener {
            val intent = Intent(this, WebServerService::class.java)
            stopService(intent) // Tells the system to stop the WebServerService
            serverStatusText.text = "Foreground service stopped"
        }
    }
}


