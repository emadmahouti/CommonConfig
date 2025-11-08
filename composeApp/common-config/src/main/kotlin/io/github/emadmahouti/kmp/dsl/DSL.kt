package io.github.emadmahouti.kmp.dsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun KotlinMultiplatformExtension.commonConfig(block: Config.() -> Unit) {
   val data = Config().apply(block)
    (this as org.gradle.api.plugins.ExtensionAware).extensions.add("commonConfigData", data)
}