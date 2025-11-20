## 手机厂商提供的测量值
来自 /system/vender/overlay/FrameworksResTarget_Vendor.apk/res/xml/power_profile.xml

### 置信度
TODO

### 误差
单独使用屏幕时 actual drain * 2.5 ~= sum(components)
TODO

### 值(mA)
- 屏幕
    - `screen.on`: 176
    - `screen.full`: 460

- CPU
    - `cpu.clusters.cores`: cluster0 = 4, cluster1 = 3, cluster2 = 1
    - `cpu.active`: 7.7
    - `cpu.idle`: 0.1
    - `cpu.suspend`: 0

- 无线与网络
    - `wifi.on`: 0.1
    - `wifi.active`: 160
    - `wifi.scan`: 1.2
    - `wifi.controller.idle`: 1
    - `wifi.controller.rx`: 176
    - `wifi.controller.tx`: 200
    - `wifi.controller.voltage`: 3700
    - `wifi.batchedscan`: 0.0001, 0.001, 0.01, 0.1, 1
    - `wifi.controller.tx_levels`: 1

    - `radio.active`: 160
    - `radio.scanning`: 5.5
    - `radio.on`: 86, 8

    - `modem.controller.idle`: 6
    - `modem.controller.rx`: 180
    - `modem.controller.tx`: 186
    - `modem.controller.voltage`: 3700

- 摄像头与传感器
    - `camera.flashlight`: 600
    - `camera.avg`: 368

- GPS 与 DSP
    - `gps.on`: 13.7
    - `dsp.audio`: 24
    - `dsp.video`: 66

- 内存与带宽
    - `memory.bandwidths`: 17

- 蓝牙
    - `bluetooth.active`: 130
    - `bluetooth.on`: 0.7
    - `bluetooth.controller.voltage`: 3700

## AOSP提供的典型值
来自 https://source.android.com/docs/core/power/values

### 置信度
TODO

### 误差
TODO

### 值
- 屏幕
    - `ambient.on`: 100 
    - `screen.on`: 200 
    - `screen.full`: 100  - 300 

- 无线与网络
    - `wifi.on`: 2 
    - `wifi.active`: 31 
    - `wifi.scan`: 100 

- 音频与视频
    - `audio`: 10 
    - `video`: 50 

- 摄像头
    - `camera.avg`: 600 
    - `camera.flashlight`: 200 

- GPS
    - `gps.signalqualitybased`: 30 , 10 
    - `gps.on`: 50 

- 蜂窝无线（Radio / Modem）
    - `radio.active`: 100  - 300 
    - `radio.scanning`: 1.2 
    - `radio.on`: 1.2 
    - `modem.controller.sleep`: 0 
    - `modem.controller.idle`:
    - `modem.controller.tx`: 100 , 200 , 300 , 400 , 500 
    - `modem.controller.voltage`:

- Wi‑Fi / 蓝牙
    - `wifi.controller.idle`:
    - `wifi.controller.rx`:
    - `wifi.controller.tx`:
    - `wifi.controller.voltage`:
    - `bluetooth.controller.idle`:
    - `bluetooth.controller.rx`:
    - `bluetooth.controller.tx`:
    - `bluetooth.controller.voltage`:

- CPU
    - `cpu.speeds`: 125000 KHz, 250000 KHz, 500000 KHz, 1000000 KHz, 1500000 KHz
    - `cpu.active`: 100 , 120 , 140 , 160 , 200 
    - `cpu.clusters.cores`: 4, 2

## 来自单独硬件的典型值
通过单独查询手机中的硬件型号来获取的典型值
https://www.gsmarena.com/oneplus_ace_5-13592.php

### 置信度
TODO

### 误差
TODO
