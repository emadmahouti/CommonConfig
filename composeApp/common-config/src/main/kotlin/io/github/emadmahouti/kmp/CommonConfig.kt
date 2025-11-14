package io.github.emadmahouti.kmp

import io.github.emadmahouti.kmp.dsl.Config
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getting
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.io.File

class CommonConfig : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.withId("org.jetbrains.kotlin.multiplatform") {
            val outputDir = File(target.buildDir, "generated/kotlin/")

            val kmp = target.extensions.getByType(KotlinMultiplatformExtension::class.java)

            val config = (kmp as org.gradle.api.plugins.ExtensionAware)
                .extensions.create("commonConfigData", Config::class.java)

            target.afterEvaluate {
//                kmp.sourceSets.all { println(this.name) }

                config.sources.forEachIndexed { index, source ->
                    val filePath = File(outputDir, source.name)
                    kmp.sourceSets.getByName(source.name).kotlin
                        .srcDir(filePath.absolutePath)

                    val generateTask = "generateKotlinClass${index}"
                    target.tasks.register(
                        generateTask,
                        GenerateKotlinClassTask::class.java
                    )
                    {
                        outputs.upToDateWhen { false }

                        if (config.packageName != null) {
                            this.source = source
                            this.packageName = config.packageName!!
                            this.outputDir = filePath
                        }
                    }

                    target.tasks.getByName("build") {
                        dependsOn(generateTask)
                    }

//                    kmp.targets.all { target ->
//                        target.compilations.all { compilation ->
////                            if(compilation.name == "main") {
//                                println(compilation.compileTaskProvider.name)
////                                compilation.compileTaskProvider.configure { dependsOn(generateTask) }
////                            }
//                            return@all true
//                        }
//                    }

                }
            }
        }
    }
}