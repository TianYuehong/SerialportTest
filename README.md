# SerialportTest
Androind_Serialport_app
使用Google开源代码，通过JNI的方式实现串口通讯。android-serialport-api的代码：  
[android-serialport-api_code](https://github.com/cepr/android-serialport-api)  
JNI支持参考：https://developer.android.com/studio/projects/add-native-code?hl=zh-cn#link-gradle  
google apk:https://code.google.com/archive/p/android-serialport-api/downloads 这个示例程序可以实现串口的收发  

# 按照如下步骤进行：  
1、安装Android studio、NDK、SDK、CMake、LLDB等。  
2、下载Google源码，有用的是android-serialport-api/project/目录。其中jni目录表示C源码和ndk-build的依赖文件，src/android_serialport_api/目录下的SerialPort.java和SerialPortFinder.java是串口类，以上是调用的关键，可以通过src/android_serialport_api/sample中的例程熟悉调用的方法。  
3、在项目名/app/src/main/java下创建android_serialport_api目录，将android-serialport-api/android-serialport-api/project/src/android_serialport_api/目录下的SerialPort.java和SerialPortFinder.java复制到android_serialport_api目录下  
4、在project状态下右键main，New->Folder->JNI Folder. 并将android-serialport-api/android-serialport-api/project/jni目录下的除了gen脚本以外的四个文件拷入刚刚创建的文件夹  
5、将 Gradle 关联到原生库中，（右键点击main add c++ module，弹出窗口选择Android.mk)使用 Android Studio UI并选择ndk-build,请使用 Project Path 旁的字段为外部 ndk-build 项目指定 Android.mk 脚本文件。如果 Application.mk 文件与您的 Android.mk 文件位于相同目录下，Android Studio 也会包含此文件。指向之前加入的jni目录下的Android.mk后，android-serialport-api对应的JNI支持就已经完成了。  

# 调用方式：
```Java
private SerialPortFinder mSerialPortFinder;  
mSerialPortFinder = new SerialPortFinder();  
String[] devices = mSerialPortFinder.getAllDevices(); //查找所有串口设备
```
