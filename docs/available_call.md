# 系统功能调用分类

## 屏幕显示
- 设置屏幕亮度
  - 命令: `su -c settings put system screen_brightness <value>`
  - 函数: `Executor.setScreenBrightness(context, value: Int)`
  - 合法值: [0, 4096]

- 设置屏幕关闭超时
  - 命令: `su -c settings put system screen_off_timeout <millis>`
  - 函数: `Executor.setScreenOffTimeout(context, millis: Int)`
  - 合法值: [8000, ?]
  - 典型值: 15000(15秒) 1800000(30分钟)

- 按下电源键
  - 命令: `su -c input keyevent KEYCODE_POWER`
  - 函数: `Executor.togglePower(context)`

- 切换屏幕刷新率
  - 命令: 
    - `su -c settings put system peak_refresh_rate <value>`
    - `su -c settings put system min_refresh_rate <value>`
    - `su -c settings put secure user_refresh_rate <value>`
  - 函数: `Executor.changeScreenRefreshRate(context, order: String)`
  - 合法值: `"30"` / `"60"` / `"90"` / `"120"`
  - 备注: 显示帧率与具体App有关, 仍需详细测量

- TODO: 切换自动亮度 [待测试]
  - 命令: `su -c settings put system screen_brightness_mode <0|1>`
  - 函数: `Executor.switchAutoBrightness(context, order: String)`
  - 合法值: `"0"` / `"1"`
  - 说明: 开启自动亮度可根据光感器动态节电；关闭可配合固定亮度更稳定节电。 [待测试]

- TODO: 切换 Doze / 常亮显示 (AOD) [待测试]
  - 命令:
    - `su -c settings put secure doze_enabled <0|1>`
    - `su -c settings put secure doze_always_on <0|1>`
  - 函数: `Executor.toggleDoze(context, order: Int)`
  - 合法值: `0` / `1`
  - 说明: 用于关闭 AOD/Doze 可节省持续显示功耗；某些厂商 key 变体需测试。 [待测试]

## 无线连接
- 切换 WIFI
  - 命令: `su -c svc wifi <order>`
  - 函数: `Executor.switchWifi(context, order: String)`
  - 合法值: `"enable"` / `"disable"`

- 切换蓝牙
  - 命令: `su -c svc bluetooth <order>`
  - 函数: `Executor.switchBluetooth(context, order: String)`
  - 合法值: `"enable"` / `"disable"`

- 切换 NFC
  - 命令: `su -c svc nfc <order>`
  - 函数: `Executor.switchNFC(context, order: String)`
  - 合法值: `"enable"` / `"disable"`

- 切换移动数据
  - 命令: `su -c svc data <order>`
  - 函数: `Executor.switchData(context, order: String)`
  - 合法值: `"enable"` / `"disable"`

- TODO: 关闭/开启 Wi‑Fi 后台扫描 [待测试]
  - 命令: `su -c settings put global wifi_scan_always_enabled <0|1>`
  - 函数: `Executor.switchWifiScanAlwaysEnabled(context, enabled: Int)`
  - 合法值: `0` / `1`
  - 说明: 关闭后可防止频繁唤醒与扫描，节省无线功耗和 CPU 唤醒。 [待测试]

- TODO: 关闭/开启 BLE 后台扫描 [待测试]
  - 命令: `su -c settings put global ble_scan_always_enabled <0|1>`
  - 函数: `Executor.switchBleScanAlwaysEnabled(context, enabled: Int)`
  - 合法值: `0` / `1`
  - 说明: 在不需要 BLE 广播/扫描时关闭可节省少量功耗；可能与蓝牙芯片和 ROM 有关。 [待测试]

- TODO: 禁用 A2DP（经典蓝牙音频）保留 BLE [待测试]
  - 命令(示例): `su -c service call bluetooth_manager 8 i32 0`  // 需要校验具体设备接口
  - 函数: `Executor.disableBluetoothA2dp(context)`
  - 说明: 保留 BLE，但关掉经典音频可显著节省蓝牙音量相关功耗。 [待测试]

## 传感器
- 切换自动旋转
  - 命令: `su -c settings put system accelerometer_rotation <order>`
  - 函数: `Executor.switchAutoRotation(context, order: String)`
  - 合法值: `"1"`/ `"0"`

- 设置GPS模式
  - 命令: `su -c settings put secure location_mode <mode>`
  - 函数: `Executor.setGPSMode(context, mode: Int)`
  - 合法值: 0-3 之间的整数（0:关闭, 1:仅设备, 2:省电, 3:高精度）

- ~~设置GNSS更新频率~~
  - **不可用**: 需要读高通开发手册
  - 命令: `su -c settings put global gnss_measurement_rate <millis>`
  - 函数: `Executor.setGnssRate(context, millis: Int)`

- TODO: 临时禁用 GPS Provider / 降低定位频率 [待测试]
  - 命令(示例): `su -c settings put secure location_providers_allowed -gps`
  - 函数: `Executor.disableGpsProviders(context, provider: String)`
  - 说明: 配合 `setGPSMode` 使用，短期内关闭可省去数百 mW。不同 Android 版本命令差异需要验证。 [待测试]

## 音量与音频
- ~~切换静音~~
  - **不可用**: 测试机有物理静音按键
  - 命令: `su -c input keyevent KEYCODE_VOLUME_MUTE`
  - 函数: `Executor.toggleVolumeMute(context)`

- 设置音频音量
  - 命令: `su -c cmd audio set-volume <streamType> <volume>`
  - 函数: `Executor.setAudio(context, streamType: Int, volume: Int)`
  - 合法值: 
      ```
      if
        streamType == 0 then ?
        streamType == 1 then ? 
        streamType == 2 then 电话响铃音量 range [0, 16]
        streamType == 3 then 媒体音量     range [0, 160]
        streamType == 4 then 闹钟音量     range [0, 16]
        streamType == 5 then 通知音量     range [0, 16]
      ```

## CPU与性能
- 启停CPU核心
  - 命令: `echo <order> > /sys/devices/system/cpu/cpu<cpuIndex>/online`
  - 函数: `Executor.setCpuCoreOnline(context, cpuIndex: Int, order: Int)`
  - 合法值: 
    - cpuIndex: [0, 7]
    - order: `1` / `0`
  - 备注: 有物理核心和线程核心的映射关系, 还需继续确定

- 设置CPU调频策略
  - 命令: `echo <order> > /sys/devices/system/cpu/cpu<cpuIndex>/cpufreq/scaling_governor`
  - 函数: `Executor.setCpuGovernor(context, cpuIndex: Int, order: String)`
  - 合法值: `"scx"` / `"walt"` / `"uag"` / `"conservative"` / `"powersave"` / `"performance"` / `"schedutil"`

## 应用管理
- 启用无障碍服务
  - 命令: `su -c settings put secure enabled_accessibility_services <serviceName>`
  - 函数: `Executor.accessibilityServiceEnable(context, serviceClass: Class<*>)`
  - 合法值: 无障碍服务类名
  - 典型值: `"com.example.suapp/com.example.suapp.ScreenUIAgentService"`

- 获取最近活跃的程序的包名
  - 命令: `"su -c dumpsys activity recents | grep 'Recent #'"`
  - 函数: `InfoHelper.getRecentAppsRaw()`
  - 备注: 输出的是生数据raw, 还需要进一步处理

- TODO: 将应用设置为 Idle (限制后台行为) [待测试]
  - 命令: `su -c am set-inactive <package> true`
  - 函数: `Executor.setAppIdle(context, packageName: String, idle: Boolean)`
  - 合法值: `true` / `false`
  - 说明: 把应用设为 idle 将大幅限制后台网络、唤醒和 JobScheduler 等，能显著节电。 [待测试]

- TODO: 阻止/限制应用后台网络或唤醒锁 [待测试]
  - 命令: `su -c cmd appops set <PACKAGE> RUN_IN_BACKGROUND deny`
  - 命令: `su -c cmd appops set <PACKAGE> WAKE_LOCK deny`
  - 函数: `Executor.setAppOpsBackground(context, packageName: String, op: String, mode: String)`
  - 合法值: `op = RUN_IN_BACKGROUND|WAKE_LOCK`, `mode = deny|ignore|allow`
  - 说明: 通过 appops 限制可在不卸载应用的情况下节省大量电量。 [待测试]

- TODO: 一键禁用应用 (临时) [待测试]
  - 命令: `su -c pm disable-user --user 0 <package>`  // 恢复: `su -c pm enable <package>`
  - 函数: `Executor.disablePackage(context, packageName: String)`
  - 合法值: `<package>`（包名）
  - 说明: 最彻底但最具破坏性的方法，适用于测试/临时场景。 [待测试]

## 系统省电调用
- TODO: 切换 Master Sync(主同步) 开关 [待测试]
  - 命令: `su -c settings put global auto_sync <0|1>`
  - 函数: `Executor.setMasterSync(context, enabled: Int)`
  - 合法值: `0` / `1`
  - 说明: 关闭同步可以减少后台网络和唤醒，影响同步体验。 [待测试]

- TODO: 切换省电模式 (Battery Saver) [待测试]
  - 命令(尝试): `su -c settings put global low_power <0|1>`
  - 备选: `su -c cmd power set-mode <MODE>`  // 厂商差异
  - 函数: `Executor.toggleBatterySaver(context, enabled: Int)`
  - 合法值: `0` / `1`
  - 说明: 会在系统级降低 CPU、限制后台和网络等多个子系统。 [待测试]

- TODO: 强制设备进入 Doze (测试/短时省电) [待测试]
  - 命令: `su -c cmd deviceidle force-idle`
  - 命令(恢复): `su -c cmd deviceidle unforce`
  - 函数: `Executor.forceDeviceIdle(context)`
  - 说明: 测试设备省电策略时有用，会暂停一部分后台任务与网络。 [待测试]

