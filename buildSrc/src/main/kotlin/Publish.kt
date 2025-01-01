object Publish {
    const val groupId = "com.aoeiuv020"
    const val version = AndroidVersions.versionName
    const val publishSourcesJar = true

    fun getArtifactId(path: String): String {
        return path.removePrefix(":").replace(":", "-")
    }

    fun getDependency(path: String): String {
        return "${groupId}:${getArtifactId(path)}:${version}"
    }

}