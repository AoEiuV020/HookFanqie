import java.util.Properties

// 加载两个properties中的变量，local会覆盖gradle,
// 另外注意编码，properties默认不是utf-8，必要时在android studio -> settings -> editor.file encodings -> 修改properties编码为utf-8，
Properties().also { properties ->
    listOf(
        "gradle.properties",
        "local.properties",
    ).forEach { fileName ->
        rootProject.projectDir.resolve(fileName).takeIf { it.isFile }?.reader()?.use { input ->
            properties.load(input)
        }
    }
}.also { properties ->
    properties.keys.forEach { key ->
        // 只有gradle.extra是settings和模块build通用的，
        gradle.extra[key.toString()] = properties.getProperty(key.toString())
    }
}