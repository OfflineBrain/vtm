package com.example.demo

import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.html.Header
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.Scroller
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.sidenav.SideNav
import com.vaadin.flow.component.sidenav.SideNavItem
import com.vaadin.flow.i18n.LocaleChangeEvent
import com.vaadin.flow.i18n.LocaleChangeObserver
import com.vaadin.flow.router.HasDynamicTitle
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.server.VaadinSession
import com.vaadin.flow.theme.lumo.LumoUtility.*
import kotlin.reflect.full.findAnnotations


class VTMLayout : AppLayout(), LocaleChangeObserver {
    private val toggle: DrawerToggle
    private val viewTitle: H2
    private val menuSpan: Span
    private val localeSelector = Select<LocaleSwitch>()

    init {
        localeSelector.apply {
            setItems(LocaleSwitch.all.values)
            isEmptySelectionAllowed = false
            val lang = VaadinSession.getCurrent()?.locale?.language
            value = LocaleSwitch.all[lang ?: "en"]

            setItemLabelGenerator { getTranslation(it.key) }
            prefixComponent = VaadinIcon.FLAG.create()
            addValueChangeListener { event ->
                if (event.oldValue == event.value ||
                    event.value.value == ui.get().session.locale
                ) {
                    return@addValueChangeListener
                }

                VaadinSession.getCurrent()?.locale = event.value.value
            }
        }

        toggle = DrawerToggle()
        viewTitle = H2(getTranslation("interface.label.title")).apply {
            addClassNames(FontSize.LARGE, Margin.NONE, Flex.GROW)
        }

        val header = Header(toggle, viewTitle, localeSelector).apply {
            addClassNames(
                AlignItems.CENTER,
                Display.FLEX,
                Padding.End.MEDIUM,
                Width.FULL
            )
        }

        addToNavbar(false, header)

        menuSpan = Span(getTranslation("interface.label.menu"))
        menuSpan.addClassNames(
            AlignItems.CENTER,
            Display.FLEX,
            FontSize.LARGE,
            FontWeight.SEMIBOLD,
            Height.XLARGE,
            Padding.Horizontal.MEDIUM
        );

        addToDrawer(menuSpan, Scroller(createSideNav()))
    }

    private fun createSideNav(): SideNav {
        val nav = SideNav()

        val mainViewNav = object : SideNavItem(
            getTranslation("interface.label.view.character.creation"),
            MainView::class.java,
            VaadinIcon.CHAT.create()
        ), LocaleChangeObserver {
            override fun localeChange(event: LocaleChangeEvent) {
                label = getTranslation("interface.label.view.character.creation")
            }
        }

        nav.addItem(mainViewNav)

        return nav
    }

    override fun afterNavigation() {
        super.afterNavigation()
        viewTitle.text = getCurrentPageTitle()
    }

    private fun getCurrentPageTitle(): String {
        when (content) {
            null -> {
                return getTranslation("interface.label.title")
            }

            is HasDynamicTitle -> {
                return (content as HasDynamicTitle).pageTitle
            }

            else -> {
                val title = content::class.findAnnotations(PageTitle::class).firstOrNull()
                return title?.value?.let { getTranslation(it) } ?: getTranslation("interface.label.title")
            }
        }
    }

    override fun localeChange(event: LocaleChangeEvent) {
        localeSelector.setItemLabelGenerator { getTranslation(it.key) }
        val currentPageTitle = getCurrentPageTitle()
        viewTitle.text = currentPageTitle
        menuSpan.text = getTranslation("interface.label.menu")
        ui.get().page.executeJs("""
            document.title = '${currentPageTitle}';
        """.trimIndent())
    }
}