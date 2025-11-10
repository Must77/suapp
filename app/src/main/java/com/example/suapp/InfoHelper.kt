package com.example.suapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.net.wifi.WifiManager
import android.nfc.NfcManager
import android.provider.Settings
import java.io.File

object InfoHelper {

/** 屏幕 **/
    fun getScreenBrightness(context: Context): Int {
        return try {
            Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS)
        } catch (e: Exception) {
            -1
        }
    }

    fun isScreenOn(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
        return powerManager.isInteractive
    }

    fun getScreenTimeout(context: Context): Int {
        return try {
            Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_OFF_TIMEOUT)
        } catch (e: Exception) {
            -1
        }
    }

/** 无线 **/
    fun isWifiEnabled(context: Context): Boolean {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.isWifiEnabled
    }

    fun isBluetoothEnabled(context: Context): Boolean {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        return bluetoothAdapter?.isEnabled == true
    }

    fun isNFCEnabled(context: Context): Boolean {
        return try {
            val nfcManager = context.applicationContext.getSystemService(Context.NFC_SERVICE) as NfcManager
            val nfcAdapter = nfcManager.defaultAdapter
            nfcAdapter != null && nfcAdapter.isEnabled
        } catch (e: Exception) {
            false
        }
    }

    fun isMobileDataEnabled(context: Context): Boolean {
        return try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
            val mobileNetworkInfo = connectivityManager.getNetworkInfo(android.net.ConnectivityManager.TYPE_MOBILE)
            mobileNetworkInfo != null && mobileNetworkInfo.isConnected
        } catch (e: Exception) {
            false
        }
    }

/** 传感器 **/
    fun isAutoRotationEnabled(context: Context): Boolean {
        return try {
            Settings.System.getInt(context.contentResolver, Settings.System.ACCELEROMETER_ROTATION) == 1
        } catch (e: Exception) {
            false
        }
    }

    fun getGpsMode(context: Context): Int {
        return try {
            Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
        } catch (e: Exception) {
            -1
        }
    }

    fun getScreenRefreshRate(context: Context): Float {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as android.view.WindowManager
        val display = windowManager.defaultDisplay
        val refreshRate = display.refreshRate
        return refreshRate
    }

/** 音量 **/    
    fun isVolumeMuted(context: Context): Boolean {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as android.media.AudioManager
        return audioManager.isStreamMute(android.media.AudioManager.STREAM_MUSIC)
    }

    fun getMediaVolume(context: Context): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as android.media.AudioManager
        return audioManager.getStreamVolume(android.media.AudioManager.STREAM_MUSIC)
    }

    fun getAlarmVolume(context: Context): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as android.media.AudioManager
        return audioManager.getStreamVolume(android.media.AudioManager.STREAM_ALARM)
    }

    fun getRingVolume(context: Context): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as android.media.AudioManager
        return audioManager.getStreamVolume(android.media.AudioManager.STREAM_RING)
    }

    fun getNotificationVolume(context: Context): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as android.media.AudioManager
        return audioManager.getStreamVolume(android.media.AudioManager.STREAM_NOTIFICATION)
    }

/** CPU **/
    fun isCpuCoreExists(cpuIndex: Int): Boolean {
        return File("/sys/devices/system/cpu/cpu$cpuIndex").exists()
    }

    fun isCpuCoreOnline(cpuIndex: Int): Boolean {
        val cpufile = File("/sys/devices/system/cpu/cpu$cpuIndex/online")
        return cpufile.readText().trim() == "1"
    }

    fun getCpuGovernor(cpuIndex: Int): String {
        val govFile = File("/sys/devices/system/cpu/cpu$cpuIndex/cpufreq/scaling_governor")
        return govFile.readText().trim()
    }

/// ====待修改=====
    fun getCpuFrequencies(): String {
        val sb = StringBuilder()
        for (i in 0..7) {
            val path = "/sys/devices/system/cpu/cpu$i/cpufreq/scaling_cur_freq"
            val file = File(path)
            if (file.exists()) {
                try {
                    val freq = file.readText().trim()
                    sb.append("CPU$i: ${freq} kHz\n")
                } catch (e: Exception) {
                    sb.append("CPU$i: error\n")
                }
            }
        }
        return sb.toString()
    }
}
