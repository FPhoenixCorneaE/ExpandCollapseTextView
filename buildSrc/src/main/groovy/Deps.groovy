class Deps {
    static gradle_version = '4.2.0'
    static kotlin_version = '1.5.0'
    static android_maven_gradle_plugin = '2.1'

    static classpath = [
            gradle      : "com.android.tools.build:gradle:$gradle_version",
            kotlin      : "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version",
            androidMaven: "com.github.dcendents:android-maven-gradle-plugin:$android_maven_gradle_plugin",
    ]

    /** Android */
    static android = [
            compileSdkVersion: 30,
            buildToolsVersion: "30.0.3",
            minSdkVersion    : 21,
            targetSdkVersion : 30,
            versionCode      : 101,
            versionName      : "1.0.1"
    ]

    /* 组件 */
    static FPhoenixCorneaE = [
            CommonToolbar: "com.github.FPhoenixCorneaE:ExpandCollapseTextView:${android.versionName}",
    ]

    /** androidX */
    static androidX = [
            appcompat       : "androidx.appcompat:appcompat:1.2.0",
            constraintLayout: "androidx.constraintlayout:constraintlayout:2.0.4",
            recyclerview: "androidx.recyclerview:recyclerview:1.1.0",
    ]

    /** Kotlin */
    static kotlin = [
            coreKtx: "androidx.core:core-ktx:1.3.2",
            stdlib : "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version",
    ]
}