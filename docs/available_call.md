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

- 启用无障碍服务
  - 命令: `su -c settings put secure enabled_accessibility_services <serviceName>`
  - 函数: `Executor.accessibilityServiceEnable(context, serviceClass: Class<*>)`
  - 合法值: 无障碍服务类名
  - 典型值: `"com.example.suapp/com.example.suapp.ScreenUIAgentService"`

- 获取最近活跃的程序的包名
  - 命令: `"su -c dumpsys activity recents | grep 'Recent #'"`
  - 函数: `InfoHelper.getRecentAppsRaw()`
  - 备注: 输出的是生数据raw, 还需要进一步处理