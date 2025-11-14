package io.github.emadmahouti.kmp.util

import org.gradle.api.Project
import java.io.File
import java.util.Properties
import kotlin.collections.set
import kotlin.reflect.KProperty

fun String.toSnakeCase(): String {
    return this.replace(Regex("([a-z])([A-Z])"), "$1_$2")
        .uppercase()
}

inline fun <T> MutableList<T>.updateFirst(
    predicate: (T) -> Boolean,
    transform: (T) -> T,
    initializer: ()  -> T
) {
    val index = indexOfFirst(predicate)
    if (index != -1) {
        this[index] = transform(this[index])
    } else {
        this.add(initializer.invoke())
    }
}

inline fun <T> MutableList<T>.updateEach(transform: (T) -> T) {
    for (i in indices) {
        this[i] = transform(this[i])
    }
}

fun Project.localProperty(): LocalProp {
    return LocalProp( this.rootProject.file("local.properties"))
}
