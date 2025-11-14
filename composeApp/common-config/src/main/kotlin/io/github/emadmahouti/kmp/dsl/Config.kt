package io.github.emadmahouti.kmp.dsl

import io.github.emadmahouti.kmp.util.updateEach
import io.github.emadmahouti.kmp.util.updateFirst
import org.gradle.launcher.daemon.protocol.Build

internal data class BuildField(
    val type: String,
    val key: String,
    val value: String
)

interface BuildConfig {
    fun buildConfigField(
        type: String,
        key: String,
        value: String? = null
    )
}

class PlatformConfiguration {
    internal var sourceCallback: ((String, Property) -> Unit)? = null

    fun getByName(sourceName: String, block: BuildConfig.() -> Unit) {
        val source = sourceName.takeIf {
            it.contains("Main")
        } ?: run {
            "${sourceName}Main"
        }

        block.invoke(object : BuildConfig {
            override fun buildConfigField(
                type: String,
                key: String,
                value: String?
            ) {
                sourceCallback?.invoke(
                    source,
                    Property(
                        modifier = Modifier.NONE,
                        field = BuildField(
                            type = type,
                            key = key,
                            value = value ?: ""
                        )
                    )
                )
            }
        })
    }
}

enum class Modifier {
    EXPECT,
    ACTUAL,
    NONE
}

internal data class SourceSet(
    val name: String,
    val modifier: Modifier,
    val properties: List<Property>
)

internal data class Property(
    val modifier: Modifier,
    val field: BuildField
)

open class Config : BuildConfig {
    internal var packageName: String? = null
    internal val sources = ArrayList<SourceSet>()
    fun packageName(value: String) = apply { packageName = value }

    fun platform(configuration: PlatformConfiguration.() -> Unit) {
        if (sources.isEmpty())
            error("Default BuildField is Required")

        val commonSourceSet = sources[0]
        sources[0] = commonSourceSet.copy(modifier = Modifier.EXPECT)

        val platform = PlatformConfiguration()

        platform.sourceCallback = { sourceName, property ->
            val index =
                commonSourceSet.properties.indexOfFirst { it.field.key == property.field.key }

            val newProperty = if (index != -1) {
                sources[0] =
                    sources[0].copy(properties = commonSourceSet.properties.mapIndexed { i, it ->
                        it.copy(modifier = Modifier.EXPECT)
                    })
                property.copy(modifier = Modifier.ACTUAL)
            } else {
                property
            }

            sources.updateFirst(
                predicate = { it.name == sourceName },
                transform = { it.copy(properties = it.properties + newProperty) },
                initializer = { SourceSet(name = sourceName, Modifier.ACTUAL, listOf(newProperty)) }
            )
        }
        configuration.invoke(platform)
    }

    override fun buildConfigField(type: String, key: String, value: String? ) {
        val prop = Property(
            modifier = Modifier.NONE,
            field = BuildField(
                type = type,
                key = key,
                value = value ?: ""
            )
        )
        sources.updateFirst(
            predicate = { it.name == "commonMain" },
            transform = { it.copy(properties = it.properties + prop) },
            initializer = { SourceSet(name = "commonMain", Modifier.NONE, listOf(prop)) }
        )
    }

    fun print() {
        println(sources)
    }
}