package io.ecosed.framework;

interface EcosedFramework {
    String getFrameworkVersion(); // 获取版本
    String getShizukuVersion(); // 获取Shizuku版本
    String getAndroidVersion(); // 获取系统API版本
    String getKernelVersion(); // 获取内核版本
    String getSystemVersion(); // 获取系统版本
    String getMachineArch(); // 获取处理器架构
    String getChineseCale(); // 获取农历
    String getOnePoem(); // 获取随机心灵鸡汤
    boolean isWatch(); // 判断是否是手表
    boolean isUseDynamicColors(); // 是否使用动态颜色
    void openDesktopSettings(); // 打开任务栏设置
    void openEcosedSettings(); // 打开框架设置
    void execCmd(String cmd); // 执行命令
}