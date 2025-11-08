package io.github.emadmahouti.kmp

import io.github.emadmahouti.kmp.dsl.Config
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.io.File

class CommonConfig : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.withId("org.jetbrains.kotlin.multiplatform") {
            val outputDir = File(target.buildDir, "generated/kotlin/commonMain")
            val kmp = target.extensions.getByType(KotlinMultiplatformExtension::class.java)
            kmp.sourceSets.getByName("commonMain").kotlin.srcDir(outputDir)

            val task = target.tasks.register("generateKotlinClass", GenerateKotlinClassTask::class.java)
            {
                outputs.upToDateWhen { false }
                val config = (kmp as org.gradle.api.plugins.ExtensionAware)
                    .extensions.findByName("commonConfigData") as? Config

                if(config != null) {
                    this.properties = config.buildFields
                    this.packageName = config.packageName!!
                    this.outputDir = outputDir
                }
            }

            target.tasks.named("preBuild") {
                dependsOn(task)
            }
//            kmp.targets.all { target ->
//                target.compilations.all { compilation ->
//                    // Make the generate task run before compilation
//                    compilation.compileKotlinTaskProvider.configure { dependsOn("generateKotlinClass") }
//                    return@all true
//                }
//            }
        }
    }
}