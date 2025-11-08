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

fun Project.localProperty(): LocalProp {
    return LocalProp( this.rootProject.file("local.properties"))
}
