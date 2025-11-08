package io.github.emadmahouti.kmp.dsl

data class BuildField(
    val type: String,
    val key: String,
    val value: String
)

class Config {
    internal var packageName: String? = null
    internal val buildFields = ArrayList<BuildField>()

    fun packageName(value: String) = apply { packageName = value }

    fun buildConfigField(
        type: String,
        key: String,
        value: String
    ) {
        buildFields.add(
            BuildField(
                type = type,
                key = key,
                value = value
            )
        )
    }
}