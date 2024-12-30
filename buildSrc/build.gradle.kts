plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

allprojects {
    project.layout.buildDirectory.set(rootDir.resolve("../build")
        .resolve(rootProject.name)
        .resolve(project.path.replace(":", ".")))
}
