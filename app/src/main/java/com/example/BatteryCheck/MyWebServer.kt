package com.example.BatteryCheck

import android.content.Context
import android.os.BatteryManager
import fi.iki.elonen.NanoHTTPD

class MyWebServer(
    private val context: Context,
    port: Int
) : NanoHTTPD(port) {

    override fun serve(session: IHTTPSession): Response {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

        // Check the request URI
        return when (session.uri) {

            // 1) If requesting /battery, return just the integer as text
            "/battery" -> {
                newFixedLengthResponse(batteryLevel.toString())
            }

            // 2) Otherwise, serve the HTML page
            else -> {
                val html = """
               <!DOCTYPE html>
               <html>
               <head>
                 <meta charset="UTF-8">
                 <title>Battery Level</title>
               </head>
               <body>
                 <h1>Battery Level (Auto-Refresh)</h1>
                 <div id="battery-level">Loading...</div>

                 <script>
                   async function updateBatteryLevel() {
                     try {
                       // 1. Fetch battery endpoint
                       const response = await fetch('/battery');
                       
                       // 2. Read the server's response text
                       const batteryValue = await response.text();
                       
                       // 3. Update the UI
                       document.getElementById('battery-level').innerText =
                         `Battery: ${'$'}{batteryValue}%`;
                     } catch (err) {
                       console.error('Error fetching battery level:', err);
                     }
                   }

                   // Refresh every 5 seconds
                   setInterval(updateBatteryLevel, 5000);
                   updateBatteryLevel(); // Fetch immediately
                 </script>
               </body>
               </html>
                     
            """.trimIndent()

                newFixedLengthResponse(html)
            }
        }
    }
}


