# 本文档介绍:
　1. 记录项目相关的资料，方便以后阅读查看
　2. 本文档为markdown语法

# 版本发布历史
版本号|版本名|发布日期|APK编译人|更新事项
---|---|---|---|---
1|v1.0.0|2020-10-21|王文斌|第一次发布测试
# 测试账号记录

环境|账号|密码|备注
---|---|---|---
debug| key0 | 123456
release | key0 | 123456

# 各部分负责人
负责人|负责部分
---|---
李宏明|项目经理
李宏明|APP 整体管理
王文斌|APP Android端
邹想中|接口

# 打包版本说明
versionCode 单独测试验证无需增加版本 发布版本包时每次 + 1
versionName 单独测试验证无需增加版本 发布版本包时每次大版本 + 1，小版本 + 0.0.1

# 安卓卡连接替换包说明
adb connect 14.215.128.98:13118
adb root && adb remount
adb push apk路径 /system/app/AndroidExpandService (应用名称需改成AndroidExpandService.apk)
adb install -r app路径

# 配网工具流程
配网工具--->下发数据14.215.128.98|12118|14.215.128.98:8011--->安卓卡代理接收服务器地址(14.215.128.98:8011)
--->服务器地址保存本地--->重启代理--->建立webSocket连接
注：配网工具版本归档路径自取

# 其他adb操作指令
adb导出logcat：adb logcat > D:/log.txt
adb导出anr文件：adb pull /data/anr/trances.txt
adb连接安卓卡：adb connect 192.168.129.11:5555
adb导出安卓卡界面：scrcpy.exe -s 192.168.129.11:5555
adb重启指定安卓卡：adb -s 192.168.129.11 reboot
adb进入安卓卡：adb -s 192.168.129.11 shell
adb进入安卓卡查看日志：getprop | grep log
adb进入安卓卡查看指定应用进程：ps | grep 应用日志
adb进入安卓卡查看当前运行的进程：top -n 10
adb进入安卓卡杀死指定进程：kill 2335
adb查看eeprom_tools文件：eeprom_tools get
adb设置eeprom_tools文件：eeprom_tools set 18 192.168.129.11
adb设置ip等信息：setip.sh 192.168.11.62 255.255.255.0 192.168.11.1 8.8.8.8
adb进入安卓卡启动应用：am start -n com.vclusters.rtc/.RtcActivity
adb进入安卓卡停止应用：am force-stop com.vclusters.rtc
adb进入安卓卡读取文件内容：cat 文件名
adb进入安卓卡查找文件： busybox find . -name "filename"
adb进入安卓卡查看应用的安装路径：dumpsys package com.tencent.mobileqq | grep Path
adb进入安卓卡设置文件的用户：chown root sdcard2
adb进入安卓卡设置文件的用户组：chgrp root sdcard2
adb进入安卓卡删除指定后缀的文件：find . -name "*.jpg" | xargs rm -f
adb进入安卓卡查看文件MD5值： md5sum /sdcard/update.zip
adb进入安卓卡恢复出厂设置：am broadcast -a android.intent.action.MASTER_CLEAR
adb进入安卓卡查看错误日志：adb logcat AndroidRuntime:E *:S
adb进入安卓卡查看进程打开文件数量：ls /proc/pid/fd | wc -l
adb查找文件下面的日志： find  -name "*"|xargs grep "mountInfo"


