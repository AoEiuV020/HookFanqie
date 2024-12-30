import org.gradle.api.JavaVersion

@Suppress("MemberVisibilityCanBePrivate", "unused")
object JvmVersions {
    val javaVersion = JavaVersion.VERSION_17
    val jvmTarget = javaVersion.toString()
    val jvmIntTarget = jvmTarget.toInt()
    val kotlinJvmTarget = jvmTarget
}