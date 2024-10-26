package com.example.demo

import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@Theme(variant = Lumo.DARK)
class DemoApplication : AppShellConfigurator

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
