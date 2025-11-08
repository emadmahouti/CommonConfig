# 🧩 CommonConfig – BuildConfigField for Kotlin Multiplatform

`CommonConfig` is a lightweight **Gradle plugin** that helps you define and generate type-safe configuration constants (similar to `BuildConfig` in Android) for **Kotlin Multiplatform projects**.

It automatically generates a Kotlin object (e.g. `Config`) inside your `commonMain` source set, so you can easily access constants from shared code.

---

## 🚀 Installation

Add the plugin to your project’s `build.gradle.kts` (or `build.gradle`):

```kotlin
plugins {
    ...
    id("org.jetbrains.kotlin.multiplatform") version "<your-kotlin-version>"
    id("io.github.emadmahouti.kmp.commonConfig") version "<latest-version>"
}
```

---

## ⚙️ Configuration Example

In your **KMP module’s `build.gradle.kts`** (e.g. `composeApp/build.gradle.kts`):

```kotlin
commonConfig {
    packageName("org.example.application")
    buildConfigField("String", "token", "\"Hello World\"")
    // You can add more fields:
    // buildConfigField("Int", "maxCount", "5")
    // buildConfigField("Boolean", "isDebug", "true")
}
```

This will generate a Kotlin file automatically at build time:

```
composeApp/build/generated/kotlin/commonMain/{packageName}/Config.kt
```

---

## 🧱 Generated Code Example

```kotlin
package org.example.application

object Config {
    const val token: String = "Hello World"
}
```

---

## 🧑‍💻 Usage in `commonMain`

Once generated, you can simply use it anywhere in your shared code:

```kotlin
import org.example.application.Config

fun printToken() {
    println(Config.token)
}
```

---

## ⚠️ Notes

- The plugin is designed for **Kotlin Multiplatform** projects using `org.jetbrains.kotlin.multiplatform`.
- Fields must be declared with valid Kotlin types:  
  `"String"`, `"Int"`, `"Boolean"`, `"Long"`, `"Float"`, `"Double"`
- The value string should include quotes if it’s a string literal (e.g. `"\"Hello\""`).

---

## 🧰 Available DSL Functions

| Function | Description | Example |
|-----------|--------------|----------|
| `packageName(name: String)` | Sets the target package for the generated file. | `packageName("org.example.config")` |
| `buildConfigField(type: String, key: String, value: String)` | Adds a constant to the generated `Config` object. | `buildConfigField("Int", "maxItems", "10")` |


