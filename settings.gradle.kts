pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    System.setProperty("maven.repo.local", rootDir.resolve("local").absolutePath)
    repositories {
        mavenLocal()
        maven(uri(rootDir.resolve("repo")))
        google()
        mavenCentral()
        maven("https://api.xposed.info/")
    }
}

rootProject.name = "HookFanqie"
listOf("app", "hook")
    .filter { rootDir.resolve(it).isDirectory }
    .forEach {
    include(":$it")
}
rootDir.resolve("sdk")
    .takeIf { it.isDirectory }
    .let { it?.listFiles() ?: emptyArray() }
    .filter { it.isDirectory }
    .map { it.name }
    .forEach {
        include(":sdk:$it")
    }

apply(rootDir.resolve("gradle/props.gradle.kts"))
apply(rootDir.resolve("gradle/project.gradle.kts"))

gradle.extra.properties.toSortedMap().forEach { (key, value) ->
    println("$key => $value")
}
