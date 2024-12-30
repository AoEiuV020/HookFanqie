object Publish {
    const val groupId = "com.aoeiuv020"
    const val version = "5.8.5.18"
    const val publishSourcesJar = true

    fun getArtifactId(path: String): String {
        return path.removePrefix(":").replace(":", "-")
    }

    fun getDependency(path: String): String {
        return "${groupId}:${getArtifactId(path)}:${version}"
    }

}