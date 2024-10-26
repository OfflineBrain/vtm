package com.example.demo

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.i18n.LocaleChangeEvent
import com.vaadin.flow.i18n.LocaleChangeObserver
import com.vaadin.flow.router.HasDynamicTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.lumo.LumoUtility.Border
import com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius

@Route(layout = VTMLayout::class)
class MainView : VerticalLayout(), LocaleChangeObserver, HasDynamicTitle {
    private val localizationContext: MutableMap<String, (String) -> Unit> = HashMap()
    private val attributeGradeSelectors: MutableMap<String, Select<String>> = HashMap()
    private val clanSelectors: Select<CharacterTemplate.Clan>
    private val bloodlineSelectors: Select<CharacterTemplate.Bloodline>

    private val characterBind = Binder(Character::class.java)

    init {

        val template = CharacterTemplate.default()

        val attributeGroups = HashMap<String, Component>()
        template.attributeGroups.forEach { group ->
            val attributes = ArrayList<IntegerField>()
            group.attributes.forEach { attribute ->
                IntegerField(getTranslation(attribute.key)).also {
                    attributes.add(it)
                    characterBind.forField(it)
                        .withValidator(
                            { value -> value <= 7 },
                            { _ -> "${getTranslation(attribute.key)} is TOO HIGH BITCH" })
                        .bind({ character -> character.getter(attribute.key) },
                            { character, value -> character.setter(attribute.key, value) })
                    localizationContext[attribute.key] = it::setLabel
                }
            }

            val select = Select<String>().apply {
                setItems(template.attributeGrades)
                setItemLabelGenerator { getTranslation(it) }
                maxWidth = "5em"
            }
            attributeGradeSelectors[group.key] = select

            attributeGroups[group.key] = attributeColumn(group.key, select, *attributes.toTypedArray())
        }

        bloodlineSelectors = Select<CharacterTemplate.Bloodline>().apply {
            label = getTranslation("character.clan.bloodline")
            localizationContext["character.clan.bloodline"] = ::setLabel

            setItemLabelGenerator { getTranslation(it.nameKey) }
            addValueChangeListener { event ->
                setTooltipText(getTranslation(event.value.descriptionKey)).also {
                    it.isManual = true
                }
            }
        }


        val bloodlineTooltip = Button(VaadinIcon.INFO.create()) {
            val tooltip = bloodlineSelectors.tooltip
            tooltip.isOpened = !tooltip.isOpened
        }

        clanSelectors = Select<CharacterTemplate.Clan>().apply {
            label = getTranslation("character.clan")
            localizationContext["character.clan"] = ::setLabel

            setItems(template.clans)
            setItemLabelGenerator { getTranslation(it.nameKey) }
            addValueChangeListener { event ->
                setTooltipText(getTranslation(event.value.descriptionKey)).also {
                    it.isManual = true
                }
                bloodlineSelectors.setItems(event.value.bloodlines)
            }
        }
        val clanTooltip = Button(VaadinIcon.INFO.create()) {
            val tooltip = clanSelectors.tooltip
            tooltip.isOpened = !tooltip.isOpened
        }

        val clan = HorizontalLayout(clanSelectors, clanTooltip, bloodlineSelectors, bloodlineTooltip).apply {
            alignItems = FlexComponent.Alignment.END
        }

        val attributes = HorizontalLayout().apply {
            add(attributeGroups.values)
        }

        add(clan, attributes)
    }

    private fun attributeColumn(group: String, gradeSelector: Component, vararg components: Component) = VerticalLayout(
        HorizontalLayout(H3(getTranslation(group)).also {
            localizationContext[group] = it::setText
        }, gradeSelector).apply {
            alignItems = FlexComponent.Alignment.CENTER
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            isPadding = false
            minWidth = "100%"
        },
        VerticalLayout(*components).apply {
            isSpacing = false
        }
    ).noSpacingWithBorder()

    private fun <T : Component> T.noSpacingWithBorder(): T = apply {
        isSpacing = false
        addClassNames(
            Border.ALL,
            BorderRadius.SMALL
        )
    }

    override fun localeChange(event: LocaleChangeEvent) {
        localizationContext.forEach { (key, updater) -> updater(getTranslation(key)) }
        attributeGradeSelectors.values.forEach { group -> group.setItemLabelGenerator { getTranslation(it) } }

        clanSelectors.apply {
            setItemLabelGenerator { getTranslation(it.nameKey) }
            value?.descriptionKey?.let { setTooltipText(getTranslation(it)) }
            addValueChangeListener { event ->
                setTooltipText(getTranslation(event.value.descriptionKey))
            }
        }

        bloodlineSelectors.apply {
            setItemLabelGenerator { getTranslation(it.nameKey) }
            value?.descriptionKey?.let { setTooltipText(getTranslation(it)) }
            addValueChangeListener { event ->
                setTooltipText(getTranslation(event.value.descriptionKey))
            }
        }
    }

    override fun getPageTitle(): String = getTranslation("interface.label.view.character.creation")
}