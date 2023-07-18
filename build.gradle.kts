buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://jitpack.io")
        mavenLocal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0-rc01")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
        classpath("com.google.gms:google-services:4.3.15")
    }
}

allprojects {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://jitpack.io")
        mavenLocal()
    }
}

// 在flutter模块中接入plugin
subprojects {
    project.evaluationDependsOn(":flutter")
}

//// flutter pub get
//tasks.create("FlutterPubGet"){
//    exec {
//        workingDir = rootProject.projectDir
//        if (System.getProperty("os.name").lowercase(java.util.Locale.ROOT).contains("windows")){
//            commandLine("cmd", "/c", "flutter", "pub", "get")
//        } else {
//            commandLine("sh", "-c", "flutter", "pub", "get")
//        }
//    }
//}