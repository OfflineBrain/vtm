package com.example.demo

import java.util.*

data class LocaleSwitch(val key: String, val value: Locale) {
    companion object {
        val all = mapOf(
            "en" to LocaleSwitch("locale.english", Locale.of("en")),
            "ru" to LocaleSwitch("locale.russian", Locale.of("ru")),
        )
    }
}