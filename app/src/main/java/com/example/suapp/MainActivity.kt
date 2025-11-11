package com.example.suapp

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var infoText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        infoText = findViewById(R.id.infoText)

    /** 绑定按钮 **/
        // ==== 基础操作 ===
        val btnRefresh: Button = findViewById(R.id.btnRefresh)

        // ==== 屏幕 ====
        val btnSetTimeout: Button = findViewById(R.id.btnSetTimeout)
        val btnToggleScreenOnOff: Button = findViewById(R.id.btnToggleScreenOnOff)
        val btnBrightnessUp: Button = findViewById(R.id.btnBrightnessUp)
        val btnBrightnessDown: Button = findViewById(R.id.btnBrightnessDown)
        val btnSetRefreshRate: Button = findViewById(R.id.btnSetRefreshRate)

        // ==== 无线 ====
        val btnToggleWifi: Button = findViewById(R.id.btnToggleWifi)
        val btnToggleBluetooth: Button = findViewById(R.id.btnToggleBluetooth)
        val btnToggleNFC: Button = findViewById(R.id.btnToggleNFC)
        val btnToggleMobile: Button = findViewById(R.id.btnToggleMobile)

        // ==== 传感器 ====
        val btnToggleAutoRotate: Button = findViewById(R.id.btnToggleAutoRotate)
        val btnGps: Button = findViewById(R.id.btnGps)

        // ==== 音量 ====
        val btnToggleVolumeMute: Button = findViewById(R.id.btnToggleVolumeMute)
        val btnSetMediaVolume: Button = findViewById(R.id.btnSetMediaVolume)
        val btnSetRingVolume: Button = findViewById(R.id.btnSetRingVolume)
        val btnSetAlarmVolume: Button = findViewById(R.id.btnSetAlarmVolume)
        val btnSetNotificationVolume: Button = findViewById(R.id.btnSetNotificationVolume)

        // ==== CPU ====
        val btnSwitchCpu4: Button = findViewById(R.id.btnSwitchCpu4)
        val btnSwitchCpu4PowerSave: Button = findViewById(R.id.btnSwitchCpu4PowerSave)

        // ==== 其他 ====
        val btnCpuLimit: Button = findViewById(R.id.btnCpuLimit)


    /** 设置按钮点击事件 **/
        // 基础
        btnRefresh.setOnClickListener { refreshSystemInfo() }

        // ==== 屏幕 ====
        btnSetTimeout.setOnClickListener { setScreenTimeout() }
        btnToggleScreenOnOff.setOnClickListener { switchScreenOnOff() }
        btnBrightnessUp.setOnClickListener { adjustBrightness(+20) }
        btnBrightnessDown.setOnClickListener { adjustBrightness(-20) }
        btnSetRefreshRate.setOnClickListener { setRefreshRate() }

        // ==== 无线 ====
        btnToggleWifi.setOnClickListener { switchWifi() }
        btnToggleBluetooth.setOnClickListener { switchBluetooth() }
        btnToggleNFC.setOnClickListener { switchNFC() }
        btnToggleMobile.setOnClickListener { switchData() }

        // ==== 传感器 ====
        btnToggleAutoRotate.setOnClickListener { switchAutoRotation() }
        btnGps.setOnClickListener { setGpsMode() }

        // ==== 音量 ====
        btnToggleVolumeMute.setOnClickListener { switchVolumeMute() }
        btnSetMediaVolume.setOnClickListener { setMediaVolume() }
        btnSetRingVolume.setOnClickListener { setRingVolume() }
        btnSetAlarmVolume.setOnClickListener { setAlarmVolume() }
        btnSetNotificationVolume.setOnClickListener { setNotificationVolume() }

        // ==== CPU ====
        btnSwitchCpu4.setOnClickListener { switchCpu4() }
        btnSwitchCpu4PowerSave.setOnClickListener { switchCpu4Mode() }
        
        // ==== 其他 ====
        btnCpuLimit.setOnClickListener { fixCpu4Frequency() }

        
        // 检查WRITE_SETTINGS权限
        if (!Settings.System.canWrite(this)) {
            Toast.makeText(this, "请授权修改系统设置权限", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        }

        // 刷新显示
        refreshSystemInfo()
        print2console()
    }

    private fun print2console() {
        println(
            """
            ======================================================
            Android版本信息:
            - API级别: ${Build.VERSION.SDK_INT}
            - 版本名称: ${Build.VERSION.RELEASE}
            - 设备型号: ${Build.MODEL}
            - 品牌: ${Build.BRAND}
            - 产品: ${Build.PRODUCT}
            ======================================================
        """.trimIndent()
        )

        """
            ======================================================
            Android版本信息:
            - API级别: 35
            - 版本名称: 15
            - 设备型号: PKG110
            - 品牌: OnePlus
            - 产品: PKG110
            ======================================================
        """.trimIndent()
    }

/** 屏幕 **/
    private fun switchScreenOnOff() {
        val isScreenOn = InfoHelper.isScreenOn(this)
        Executor.togglePower(this)
    }

    private fun setScreenBrightness() {
        val brightness = InfoHelper.getScreenBrightness(this)
        Executor.setScreenBrightness(this, 2048) // range 0 to 4096
    }

    private fun setScreenTimeout() {
        val timeout = InfoHelper.getScreenTimeout(this)
        Executor.setScreenOffTimeout(this, 30000) // milliseconds
    }

    private fun setRefreshRate() {
        val rate = InfoHelper.getScreenRefreshRate(this)
        Executor.changeScreenRefreshRate(this, "30")
    }

/** 无线 **/
    private fun switchWifi() {
        val isWifiEnabled = InfoHelper.isWifiEnabled(this)
        val order = if (isWifiEnabled) {
            "disable"
        } else {
            "enable"
        }

        Executor.switchWifi(this, order)
    }

    private fun switchBluetooth() {
        val isBluetoothEnabled = InfoHelper.isBluetoothEnabled(this)
        val order = if (isBluetoothEnabled) {
            "disable"
        } else {
            "enable"
        }
        println("isBluetoothEnabled: $isBluetoothEnabled")
        println("order: $order")
        Executor.switchBluetooth(this, order)
    }

    private fun switchNFC(){
        val isNFCEnabled = InfoHelper.isNFCEnabled(this)
        val order = if (isNFCEnabled) {
            "disable"
        } else {
            "enable"
        }

        Executor.switchNFC(this, order)
    }

    private fun switchData(){
        val isDataEnabled = InfoHelper.isMobileDataEnabled(this)
        val order = if (isDataEnabled) {
            "disable"
        } else {
            "enable"
        }

        Executor.switchData(this, order)
    }

/** 传感器 **/
    private fun switchAutoRotation() {
        val isAutoRotationEnabled = InfoHelper.isAutoRotationEnabled(this)
        val order = if (isAutoRotationEnabled) {
            "0"
        } else {
            "1"
        }

        Executor.switchAutoRotation(this, order)
    }

    private fun setGpsMode() {
        val mode = InfoHelper.getGpsMode(this)
        val order = if (mode != 0) {
            0
        } else {
            3
        }
        Executor.setGPSMode(this, order) // range 0 to 3
    }

    private fun setGnssRate() {
        val rate = InfoHelper.getGnssRate(this)
        Executor.setGnssRate(this, 1000) // milliseconds
    }


/** 音量 **/
    private fun switchVolumeMute() {
        // 似乎和oneplus的物理按键不兼容, 会闪退
        val isMuted = InfoHelper.isVolumeMuted(this)
        Executor.toggleVolumeMute(this)
    }

    private fun setRingVolume() {
        val ringVolume = InfoHelper.getRingVolume(this)
        Executor.setAudio(this, 2, 5)  // range 0 to 16
    }

    private fun setMediaVolume() {
        val mediaVolume = InfoHelper.getMediaVolume(this)
        Executor.setAudio(this, 3, 50)  // range 0 to 160
    }

    private fun setAlarmVolume() {
        val alarmVolume = InfoHelper.getAlarmVolume(this)
        Executor.setAudio(this, 4, 5)  // range 0 to 16
    }

    private fun setNotificationVolume() {
        val notificationVolume = InfoHelper.getNotificationVolume(this)
        Executor.setAudio(this, 5, 5)  // range 0 to 16
    }

/** CPU **/
    private fun switchCpu4() {
        if (!InfoHelper.isCpuCoreExists(4)) {
            Toast.makeText(this, "CPU4 核心不存在", Toast.LENGTH_SHORT).show()
            return
        }

        val isOnline = InfoHelper.isCpuCoreOnline(4)
        val order = if(isOnline){
            0
        } else {
            1
        }
        Executor.setCpuCoreOnline(this, 4, order)
    }

    private fun switchCpu4Mode() {
        if (!InfoHelper.isCpuCoreExists(4)) {
            Toast.makeText(this, "CPU4 核心不存在", Toast.LENGTH_SHORT).show()
            return
        }

        val mode = InfoHelper.getCpuGovernor(4)
        val order = when (mode) {
            "powersave" -> "uag"
            "uag" -> "powersave"
            else -> "uag"
        }
        Executor.setCpuGovernor(this, 4, order)
    }

    @Deprecated(message = "不支持 governor = userspace")
    private fun fixCpu4Frequency() {
        val isExists = InfoHelper.isCpuCoreExists(4)
        if (!isExists) {
            Toast.makeText(this, "CPU4 核心不存在", Toast.LENGTH_SHORT).show()
            return
        }
        
        Executor.fixCpuFrequency(this, 4, 1200000) // 1.2GHz
    }

// ================= 

    private fun adjustBrightness(delta: Int) {
        var current = InfoHelper.getScreenBrightness(this)
        current = (current + delta).coerceIn(0, 255)
        Executor.setScreenBrightness(this, current)
        Toast.makeText(this, "亮度设置为 $current", Toast.LENGTH_SHORT).show()
        refreshSystemInfo()
    }

    private fun refreshSystemInfo() {
        Thread {
            val info = StringBuilder()
            info.append("🔆 屏幕亮度: ${InfoHelper.getScreenBrightness(this)}\n")
            info.append("📶 WiFi: ${InfoHelper.isWifiEnabled(this)}\n")
            info.append("🟦 蓝牙: ${InfoHelper.isBluetoothEnabled(this)}\n")
            info.append("⚙️ CPU 频率:\n${InfoHelper.getCpuFrequencies()}\n")
            info.append("📍 GNSS 刷新率: ${InfoHelper.getGnssRate(this)} ms\n")
            info.append("🔄 屏幕刷新率: ${InfoHelper.getScreenRefreshRate(this)} Hz\n")
            info.append("> 最近的程序:\n${InfoHelper.getRecentAppsRaw()}\n")
            runOnUiThread { infoText.text = info.toString() }
        }.start()
    }
}
