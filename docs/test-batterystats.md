## 远程adb连接
```sh
adb pair 192.168.31.155:35435
> 6 PIN input
adb connect 192.168.31.155:xxxxx
```

## 传输文件
```sh
adb push hostpath phonepath
adb pull phonepath hostpath
```

## 测量模型
有两种测量策略：基于预设模型的测量, 基于硬件抽象层基于硬件电气参数的测量。

基于预设模型的测量是厂商对所有硬件预先测定好一个每小时耗电量值，该文件称为power_profile.xml，使用dumpsys batterystats测量的是各个硬件活动时间。计算`时间 *  耗电率 = 耗电量`

- 占位符：/system/framework/framework-res.apk/res/xml/power_profile.xml
- 实际参数：/system/vender/overlay/FrameworksResTarget_Vendor.apk/res/xml/power_profile.xml

基于硬件电气参数的测量 TODO


## 基于预设模型的测量
```sh
# 清空测量数据
adb shell dumpsys batterystats --reset

# 工作负载, 手动设置循环播放 + 满亮度
#   1080p30 mp4 = H264 + AAC 码率约3000kb/s
adb shell am start -n org.videolan.vlc/org.videolan.vlc.gui.video.VideoPlayerActivity \
                -a android.intent.action.VIEW \
                -d file:///sdcard/Download/test.mp4 \
                --ez from_start true

# 保持运行1小时
sleep 3600

# 转储人类可读数据（建议带日期和说明）
adb shell dumpsys batterystats > batterystats$(date +%m%d%H%M)-screen-full-60min.txt

# 转储程序可读数据
adb bugreport > bugreport$(date +%m%d%H%M)-screen-full-60min.zip
```

## 可视化batterystats.txt
从源码编译和从docker编译 https://github.com/google/battery-historian, 一个可视化 bugreport 数据的程序. 但是均无法解析数据, 可能是因为该仓库 public achieve 过早, 不支持新版 Android 生成的 bug report 了. 因此需要换用其他方法.

## 备注
    