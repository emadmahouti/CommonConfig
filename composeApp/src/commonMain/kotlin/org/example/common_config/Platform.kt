package org.example.common_config

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform