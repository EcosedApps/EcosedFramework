// 应用Flutter相关配置
apply {
    from("flutter.gradle")
}
// 项目名称
rootProject.name = "Ecosed Framework"
// 导入app模块
include(":app")