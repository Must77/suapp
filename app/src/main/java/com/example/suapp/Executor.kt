package com.example.suapp

import android.Manifest
import android.content.Context
import android.net.wifi.WifiManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresPermission
import java.io.DataOutputStream

object Executor {

/** 屏幕 **/
    fun setScreenBrightness(context: Context, value: Int) {
        val command = "su -c settings put system screen_brightness $value"
        val process = Runtime.getRuntime().exec(command)
        val exitCode = process.waitFor()
    }

    fun setScreenOffTimeout(context: Context, millis: Int) {
        val command = "su -c settings put system screen_off_timeout $millis"
        val process = Runtime.getRuntime().exec(command)
        val exitCode = process.waitFor()
    }

    fun togglePower(context: Context) {
        val command = "su -c input keyevent KEYCODE_POWER"
        val process = Runtime.getRuntime().exec(command)
    }

    fun changeScreenRefreshRate(context: Context, order: String): Int {
        val value = if (order == "120") {
            "120"
        } else if (order == "90") {
            "90"
        } else if (order == "30") {
            "30"
        } else {
            "60"
        }
        var exitCode = 0

        val command = "su -c settings put system peak_refresh_rate $value"
        val command2 = "su -c settings put system min_refresh_rate $value"
        val command3 = "su -c settings put secure user_refresh_rate $value"
        val process = Runtime.getRuntime().exec(command)
        val process2 = Runtime.getRuntime().exec(command2)
        val process3 = Runtime.getRuntime().exec(command3)
        exitCode = exitCode or process.waitFor()
        exitCode = exitCode or process2.waitFor()
        exitCode = exitCode or process3.waitFor()

        return exitCode
    }

/** 无线 */
    fun switchWifi(context: Context, order: String): Int {
        val command = "su -c svc wifi $order"
        val process = Runtime.getRuntime().exec(command)
        val exitCode = process.waitFor()
        return exitCode
    }

    fun switchBluetooth(context: Context, order: String): Int {
        val command = "su -c svc bluetooth $order"
        val process = Runtime.getRuntime().exec(command)
        val exitCode = process.waitFor()
        return exitCode
    }

    fun switchNFC(context: Context, order: String): Int {
        val command = "su -c svc nfc $order"
        val process = Runtime.getRuntime().exec(command)
        val exitCode = process.waitFor()
        return exitCode
    }

    fun switchData(context: Context, order: String): Int {
        val command = "su -c svc data $order"
        val process = Runtime.getRuntime().exec(command)
        val exitCode = process.waitFor()
        return exitCode
    }

/** 传感器 **/
    fun switchAutoRotation(context: Context, order: String): Int {
        val command = "su -c settings put system accelerometer_rotation $order"
        val process = Runtime.getRuntime().exec(command)
        val exitCode = process.waitFor()
        return exitCode
    }

    fun setGPSMode(context: Context, mode: Int): Int {
        val command = "su -c settings put secure location_mode $mode"
        val process = Runtime.getRuntime().exec(command)
        val exitCode = process.waitFor()
        return exitCode
    }

/** 音量 **/
    fun toggleVolumeMute(context: Context) {
        val command = "su -c input keyevent KEYCODE_VOLUME_MUTE"
        val process = Runtime.getRuntime().exec(command)
        val exitCode = process.waitFor()
    }

    fun setAudio(context: Context, streamType: Int, volume: Int) {
        val command = "su -c cmd audio set-volume $streamType $volume"
        val process = Runtime.getRuntime().exec(command)
        val exitCode = process.waitFor()
    }

// ======待修改=====
    fun setCpuFrequency(cpuIndex: Int, freqKHz: Int) {
        execRoot("echo $freqKHz > /sys/devices/system/cpu/cpu$cpuIndex/cpufreq/scaling_setspeed")
    }

    fun setCpuCoreOnline(cpuIndex: Int, online: Boolean) {
        val value = if (online) "1" else "0"
        execRoot("echo $value > /sys/devices/system/cpu/cpu$cpuIndex/online")
    }

    private fun execRoot(cmd: String) {
        try {
            val process = Runtime.getRuntime().exec("su")
            val os = DataOutputStream(process.outputStream)
            os.writeBytes("$cmd")
            os.writeBytes("exit")
            os.flush()
            os.close()
            process.waitFor()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
