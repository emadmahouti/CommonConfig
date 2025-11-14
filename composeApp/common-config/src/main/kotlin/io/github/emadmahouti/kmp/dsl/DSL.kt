package io.github.emadmahouti.kmp.dsl
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun KotlinMultiplatformExtension.commonConfig(block: Config.() -> Unit) {
    (this as org.gradle.api.plugins.ExtensionAware).extensions
        .configure<Config>("commonConfigData", ) {
            block()
        }
}