package io.github.emadmahouti.kmp

import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import io.github.emadmahouti.kmp.dsl.BuildField
import io.github.emadmahouti.kmp.dsl.Config
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.io.File

abstract class GenerateKotlinClassTask: DefaultTask() {


    @get:Internal
    var properties: List<BuildField> = emptyList()

    @Input
    lateinit var packageName: String

    @OutputDirectory
    lateinit var outputDir: File

    @TaskAction
    fun generate() {
        if (!outputDir.exists()) outputDir.mkdirs()

        val groupedByPackage = properties.groupBy { packageName }
        groupedByPackage.forEach { (pkg, props) ->
            val typeSpec = TypeSpec.objectBuilder("Config")
                .apply {
                    props.forEach { prop ->
                        addProperty(
                            PropertySpec.builder(prop.key, mapType(prop.type))
                                .initializer(prop.value)
                                .addModifiers(KModifier.CONST)
                                .build()
                        )
                    }
                }
                .build()

            FileSpec.builder(pkg, "Config")
                .addType(typeSpec)
                .build()
                .writeTo(outputDir)
        }

//        properties.forEach { item ->
//            val propSpec = PropertySpec
//                .builder(item.key, mapType(item.type))
//                .initializer(item.value)
//                .build()
//        }
//
//
//        val generatedClass = TypeSpec.objectBuilder("Config")
//            .addProperty(propSpec)
//            .build()
//
//        val fileSpec = FileSpec.builder(packageName, "Config")
//            .addType(generatedClass)
//            .build()
//
//        fileSpec
//            .writeTo(outputDir)
    }


    private fun mapType(typeString: String): TypeName {
        return when (typeString.lowercase()) {
            "string" -> STRING
            "int", "integer" -> INT
            "boolean", "bool" -> BOOLEAN
            "long" -> LONG
            "float" -> FLOAT
            "double" -> DOUBLE
            else -> ClassName.bestGuess(typeString)
        }
    }
}