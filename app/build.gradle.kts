plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.dragon.read"
    compileSdk = AndroidVersions.compileSdk

    defaultConfig {
        applicationId = "com.dragon.read"
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
project.afterEvaluate {
    val assetsReplaceMap = gradle.extra.properties.filter { (key, value) ->
        key.startsWith("assets.") && value is String
    }.mapValues {
        it.value as String
    }.mapValues {
        File(it.value)
    }.mapKeys { (key, _) ->
        key.removePrefix("assets.")
    }.filter {
        it.value.isFile
    }.onEach { (key, value) ->
        println("assets.$key = \"$value\"")
    }
    tasks.filter { it.name.matches(Regex("merge.*Assets")) }.forEach { mergeAssetsTask ->
        mergeAssetsTask.doLast {
            val outputs = mergeAssetsTask.outputs
            val files = outputs.files.files
            val assetsFolder = files.first()
            assetsReplaceMap.forEach { (fileName, file) ->
                val to = assetsFolder.resolve(fileName)
                file.copyTo(to, overwrite = true)
                println("copy assets/$fileName from '$file' to '${to.relativeTo(rootDir)}'")
            }
        }
    }
}

// pc端的单元测试移除无法使用的slf4j-android，
// 关键是runtimeOnly依赖不只加入apk中，test也会加上，
configurations
    .filter { it.name.startsWith("test") }
    .forEach { conf ->
        conf.exclude(module = "slf4j-android")
    }
dependencies {
    implementation(platform(libs.slf4j.bom))
    implementation(libs.slf4j)
    runtimeOnly(libs.slf4j.android)

    testImplementation(libs.slf4j.simple)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

apply(rootDir.resolve("gradle/signing.gradle"))
