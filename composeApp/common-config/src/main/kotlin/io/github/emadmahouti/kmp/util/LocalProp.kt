package io.github.emadmahouti.kmp.util

import java.io.File
import java.util.Properties
import kotlin.reflect.KProperty

public class LocalProp(private val file: File) {
    private var properties = Properties()
    operator fun getValue(thisRef: Any, property: KProperty<*>): String  {
        properties = Properties().apply {
            if (file.exists()) {
                file.inputStream().use { load(it) }
            }
        }

        if(!properties.containsKey(property.name.toSnakeCase())) {
//            file.appendText("${property.name.toSnakeCase()} = \"\"\n")
            properties[property.name.toSnakeCase()] = ""
            file.writer().use {
                properties.store(it, null)
            }
        }


        return properties?.getProperty(property.name.toSnakeCase()) ?: ""
    }
}