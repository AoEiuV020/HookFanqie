// 从properties读取模块路径，
// 依赖props.gradle.kts脚本读取properties到extra，
// 判断过滤掉路径为空或者文件夹不存在的模块，
// 存在的模块添加到项目中， 通过findProject判断是否存在，
gradle.extra.properties.filter {
    it.key.startsWith("module.") && it.value is String
}.mapValues {
    it.value as String
}.filter {
    it.value.isNotBlank()
}.filter {
    rootDir.resolve(it.value).isDirectory
}.mapKeys {
    it.key.removePrefix("module.")
}.forEach { (key, value) ->
    val path = ":" + key.replace(".", ":")
    include(path)
    project(path).projectDir = rootDir.resolve(value)
}
