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
import io.github.emadmahouti.kmp.dsl.Property
import io.github.emadmahouti.kmp.dsl.SourceSet
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.io.File
import javax.lang.model.element.Modifier

abstract class GenerateKotlinClassTask: DefaultTask() {


    @get:Internal
    internal lateinit var source: SourceSet

    @get: Input
    internal lateinit var packageName: String

    @get: OutputDirectory
    internal lateinit var outputDir: File

    @TaskAction
    fun generate() {
        if (!outputDir.exists()) outputDir.mkdirs()
        val properties = source.properties

        val groupedByPackage = properties.groupBy { packageName }
        groupedByPackage.forEach { (pkg, props) ->
            val typeSpec = TypeSpec
                .objectBuilder("Config")
                .addModifiers(mapModifier(source.modifier))
                .apply {
                    props.forEach { prop ->
                        addProperty(
                            PropertySpec.builder(prop.field.key, mapType(prop.field.type))
                                .also {
                                    if(
                                        source.modifier !== io.github.emadmahouti.kmp.dsl.Modifier.EXPECT&&
                                        prop.modifier !== io.github.emadmahouti.kmp.dsl.Modifier.EXPECT )
                                        it.initializer(prop.field.value)
                                }
                                .addModifiers(mapModifier(prop.modifier))
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

    private fun mapModifier(modifier: io.github.emadmahouti.kmp.dsl.Modifier): KModifier {
        return when(modifier) {
            io.github.emadmahouti.kmp.dsl.Modifier.EXPECT -> KModifier.EXPECT
            io.github.emadmahouti.kmp.dsl.Modifier.ACTUAL -> KModifier.ACTUAL
            io.github.emadmahouti.kmp.dsl.Modifier.NONE -> KModifier.CONST
        }
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