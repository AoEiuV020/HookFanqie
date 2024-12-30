plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "cc.aoeiuv020.hookfanqie"
    compileSdk = AndroidVersions.compileSdk

    defaultConfig {
        applicationId = "cc.aoeiuv020.hookfanqie"
        minSdk = AndroidVersions.minSdk
        targetSdk = AndroidVersions.targetSdk
        versionCode = AndroidVersions.versionCode
        versionName = AndroidVersions.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        // 从properties读取字段插入到BuildConfig，
        // 依赖props.gradle.kts脚本读取properties到extra，
        // 不过滤空的值，保留空字符串，
        // 编译时的任务generateDebugBuildConfig之后生效，
        gradle.extra.properties.filter {
            it.key.startsWith("field.") && it.value is String
        }.mapValues {
            it.value as String
        }.mapKeys {
            it.key.removePrefix("field.")
        }.forEach { (key, value) ->
            println("BuildConfig.$key = \"$value\"")
            buildConfigField("String", key, "\"" + value + "\"")
        }
        // 从properties读取字段插入到manifestPlaceholders
        gradle.extra.properties.filter { (key, value) ->
            key.startsWith("manifest.") && value is String
        }.mapValues {
            it.value as String
        }.mapKeys { (key, _) ->
            key.removePrefix("manifest.")
        }.forEach { (key, value) ->
            println("manifest.$key = \"$value\"")
            manifestPlaceholders[key] = value
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JvmVersions.javaVersion
        targetCompatibility = JvmVersions.javaVersion
    }
    kotlinOptions {
        jvmTarget = JvmVersions.jvmTarget
    }
    buildFeatures {
        compose = false
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
dependencies {
    compileOnly("de.robv.android.xposed:api:82")
}

apply(rootDir.resolve("gradle/signing.gradle"))
