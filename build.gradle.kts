// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}
allprojects {
    project.layout.buildDirectory.set(rootDir.resolve("build").resolve(project.path.replace(":", ".")))
    group = Publish.groupId
    version = System.getenv("BUILD_VERSION") ?: Publish.version
}
