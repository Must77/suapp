# 概述
我们难以获取到手机中各个硬件的实时的电气参数, 因此无法基于电气参数来估计手机的可用时长. 取而代之的使用测量模型来估计耗电情况, 虽然存在一定误差, 但是如果误差稳定可以乘以一个误差系数得到相对精确的值, 如果误差不稳定的话, 也可以不将测量模型作为对未来估计的依据, 而是作为某个硬件耗电百分比(占手机整体)的参照, 反而是对过去状态的分析.

想要通过大量实现测定power_profile.xml是不现实的, 当硬件组装在手机中时就不再能单独测定某个硬件的电气参数, 始终要根据一个baseline来测量变化值

这样多次fall-back, 还有一条可行的方案, 读取电池的电气参数. 这些参数通常比较容易读取, 但是值得注意的问题时, 这些电气参数都是实时值, 而非累计值, 要将实时值转化为累计值或者其他类型的值, 需要经过一些数学运算, 比如 mAh = mA 在 T 上的积分. 这些数学运算容易出问题

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
有三种测量策略：基于预设模型的测量, 基于硬件抽象层, 基于电池电气参数的测量。

基于预设模型的测量是厂商对所有硬件预先测定好一个每小时耗电量值，该文件称为power_profile.xml，使用dumpsys batterystats测量的是各个硬件活动时间。计算`时间 *  耗电率 = 耗电量`

    - 占位符：/system/framework/framework-res.apk/res/xml/power_profile.xml
    - 实际参数：/system/vender/overlay/FrameworksResTarget_Vendor.apk/res/xml/power_profile.xml

基于硬件抽象层的测量是AOSP提供了一些对硬件的抽象接口, 由手机厂家确定要不要实现这些接口. 而oneplus并未实现这个称为IPowerStats.hal的硬件抽象层, 根本原因可能是因为缺少硬件的片上传感器

基于电池电气参数的测量是手机通常对电池的充放电的电气参数有传感器基础. 可以在文件系统中直接读取到实时的电气参数的值.

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
# 或者?
#adb bugreport bugreport$(date +%m%d%H%M)-screen-full-60min.zip
```

## 基于hal硬件抽象层
oneplus没有实现IPowerStats.hal, 无法读取片上功耗
```sh
su
lshal | grep power
lshal | grep stats
```

## 基于电池电气参数的测量
只能读取一个总的电流电压
```sh
# 电流 单位疑似是mA 正值是耗电
su -c "cat /sys/class/power_supply/battery/current_now"
```

## 可视化batterystats.txt
从源码编译和从docker编译 https://github.com/google/battery-historian, 一个可视化 bugreport 数据的程序. 但是均无法解析数据, 可能是因为该仓库 public achieve 过早, 不支持新版 Android 生成的 bug report 了. 因此需要换用其他方法.

## 备注
    