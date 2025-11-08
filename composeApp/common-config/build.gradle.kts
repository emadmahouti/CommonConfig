plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

dependencies {
    implementation("com.squareup:kotlinpoet:1.14.2")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0")
}

gradlePlugin {
    plugins {
        create("configPlugin") {
            implementationClass = "io.github.emadmahouti.kmp.CommonConfig"
            displayName = ""
            description = ""
            tags = listOf("")
            id = "io.github.emadmahouti.kmp.commonConfig"
        }
    }
}