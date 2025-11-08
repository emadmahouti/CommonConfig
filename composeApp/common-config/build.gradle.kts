plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "1.3.0"
}

group = "io.github.emadmahouti.kmp"
version = "1.0.0"

dependencies {
    implementation("com.squareup:kotlinpoet:1.14.2")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0")
}

gradlePlugin {
    plugins {
        create("configPlugin") {
            id = "io.github.emadmahouti.kmp.commonConfig"
            implementationClass = "io.github.emadmahouti.kmp.CommonConfig"
            displayName = "CommonConfig Plugin"
            description = "Gradle plugin for Kotlin Multiplatform projects to generate type-safe build constants."
            tags = listOf("kmp", "cmp", "kotlinMultiplatform", "buildConfig")
        }
    }
    website = "https://github.com/emadmahouti/CommonConfig"
    vcsUrl = "https://github.com/emadmahouti/CommonConfig"
}