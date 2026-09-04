package com.example.wifi

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var wifiManager: WifiManager
    private lateinit var wifiSwitch: Switch

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        wifiSwitch = Switch(this).apply {
            text = "Wi-Fi Control"
            textSize = 18f
            setPadding(50, 50, 50, 50)
        }

        setContentView(wifiSwitch)

        updateSwitchState()

        wifiSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (checkLocationPermission()) {
                toggleWiFi(isChecked)
            } else {
                requestLocationPermission()
                wifiSwitch.isChecked = !isChecked
            }
        }
    }

    private fun toggleWiFi(enable: Boolean) {
        @Suppress("DEPRECATION")
        wifiManager.isWifiEnabled = enable
        val status = if (enable) "enabled" else "disabled"
        Toast.makeText(this, "Wi-Fi $status", Toast.LENGTH_SHORT).show()
    }

    private fun updateSwitchState() {
        @Suppress("DEPRECATION")
        wifiSwitch.isChecked = wifiManager.isWifiEnabled
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onResume() {
        super.onResume()
        updateSwitchState()
    }
}
