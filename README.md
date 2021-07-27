# 喂食器APP应用文档
涉及到的自有库地址

1. SipLib：

    当前使用版本：`com.github.hanreality:SipLib:1.1.0`

    地址：[https://github.com/hanreality-organization/SipLib](https://github.com/hanreality-organization/SipLib)

2. WeekCalendar:

    当前使用版本：`com.github.hanreality:WeekCalendar:1.6.4`

    地址：[https://github.com/hanreality-organization/WeekCalendar](https://github.com/hanreality-organization/WeekCalendar)

3. WifiManager：

    当前使用版本：`com.github.hanreality-organization:WifiManager:1.0.4`

    地址：[https://github.com/hanreality-organization/WifiManager](https://github.com/hanreality-organization/WifiManager)

涉及到重要的第三方库有

1. 网络库：okhttp3
2. 本地持久化储存：腾讯mmkv
3. 页面路由：阿里ARouter
4. 图片加载：Glide

工程已实现模块化，已有模块：`basesdk`、`fastvideoplayer`、`module_circle`、`module_compat`、`module_feed`、`module_home`、`module_member`、`module_message`、`module_router`、`module_sip`、`module_video`

### basesdk

一些通用的组件

### fastvideoplayer

RTMP流视频相关库

### module_circle

宠友圈（app上暂时没有透出）

### module_compat

包含启动页，首页和app启动时候的一些处理

### module_home

首页模块，即首页底部第一个tab

### module_feed

喂食模块，即首页底部第二个tab

### module_video

视频模块，即首页底部第三个tab

### module_member

个人中心模块，即首页底部第四个tab

### module_message

空模块，未使用

### module_sip

Sip信令模块

### module_router

页面路由模块

## 如何发布新版本（mac系统）

1. `base.gradle`文件中配置了版本号和版本名，当开发完成时或者着手开发时需要对配置中的`versionCode` 和 `versionName` 进行修改。
2. [build.sh](http://budil.sh) 脚本是会将app进行打包并上传，因此只需在命令行中执行

    ```bash
    sh build.sh
    ```
