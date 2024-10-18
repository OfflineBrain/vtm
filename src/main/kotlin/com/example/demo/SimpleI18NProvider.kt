package com.example.demo

import com.vaadin.flow.i18n.I18NProvider
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import java.util.*

@Component
class SimpleI18NProvider(val messageSource: MessageSource) : I18NProvider {
    override fun getProvidedLocales(): List<Locale> {
        return Locale.availableLocales().filter { messageSource.getMessage("test", null, null, it) != null }.toList()
    }

    override fun getTranslation(key: String, locale: Locale, vararg params: Any?): String? {
        return messageSource.getMessage(key, params, "!${key}!",locale)
    }
}