package com.example.demo

class CharacterTemplate(
    val attributeGrades: List<String> = listOf(
        "character.attributes.primary",
        "character.attributes.secondary",
        "character.attributes.tertiary"
    ),
    val attributeGroups: MutableList<AttributeGroup> = ArrayList(),
    val clans: MutableList<Clan> = ArrayList(),
) {

    class AttributeGroup(val key: String) {
        val attributes: MutableList<Attribute> = mutableListOf()
    }

    data class Attribute(val key: String)

    class Clan(val name: String, val clanGroup: ClanGroup) {
        val bloodlines: MutableList<Bloodline> = mutableListOf()
        val curses: MutableList<ClanCurse> = mutableListOf()
        val nameKey = clanName(name)
        val descriptionKey = clanDescription(name)
    }

    enum class ClanGroup(val key: String) {
        HIGH_CLAN("character.clans.high"),
        LOW_CLAN("character.clans.low"),

        HIGH_CLASS("character.clans.low"),
        MIDDLE_CLASS("character.clans.low"),
        LOW_CLASS("character.clans.low"),
    }

    class ClanCurse(name: String, clan: String) {
    }

    class Discipline(name: String) {
    }

    class DisciplinePoint(rating: Int, discipline: String) {
        val alternatives: MutableList<String> = mutableListOf()
    }

    class Bloodline(name: String, clan: String) {
        val nameKey = bloodlineName(clan, name)
        val descriptionKey = bloodlineDescription(clan, name)
        val curses: MutableList<ClanCurse> = mutableListOf()
    }

    companion object {
        fun default() = CharacterTemplate(
            attributeGroups = mutableListOf(
                AttributeGroup("character.attribute.physical").apply {
                    attributes.add(Attribute("character.attribute.strength"))
                    attributes.add(Attribute("character.attribute.dexterity"))
                    attributes.add(Attribute("character.attribute.stamina"))
                },
                AttributeGroup("character.attribute.social").apply {
                    attributes.add(Attribute("character.attribute.charisma"))
                    attributes.add(Attribute("character.attribute.manipulation"))
                    attributes.add(Attribute("character.attribute.appearance"))
                },
                AttributeGroup("character.attribute.mental").apply {
                    attributes.add(Attribute("character.attribute.intelligence"))
                    attributes.add(Attribute("character.attribute.perception"))
                    attributes.add(Attribute("character.attribute.wits"))
                },
            ),

            clans = mutableListOf(
                Clan("assamite", ClanGroup.LOW_CLAN).apply {
                    bloodlines.add(Bloodline("bedouin", name))
                },
                Clan("brujah", ClanGroup.HIGH_CLAN).apply {
                },
                Clan("followers-of-set", ClanGroup.LOW_CLAN).apply {
                },
                Clan("gangrel", ClanGroup.LOW_CLAN).apply {
                },
                Clan("giovanni", ClanGroup.HIGH_CLAN).apply {
                },
                Clan("lasombra", ClanGroup.HIGH_CLAN).apply {
                },
                Clan("malkavian", ClanGroup.LOW_CLAN).apply {
                },
                Clan("nosferatu", ClanGroup.LOW_CLAN).apply {
                },
                Clan("ravnos", ClanGroup.LOW_CLAN).apply {
                },
                Clan("toreador", ClanGroup.HIGH_CLAN).apply {
                },
                Clan("tremere", ClanGroup.LOW_CLAN).apply {
                },
                Clan("tzimisce", ClanGroup.HIGH_CLAN).apply {
                    bloodlines.add(Bloodline("main-branch", name))
                },
                Clan("ventrue", ClanGroup.HIGH_CLAN).apply {
                },
            )
        ).also { it.printKeys() }

        private fun clanName(clan: String) = "character.clan.$clan.name"
        private fun clanDescription(clan: String) = "character.clan.$clan.description"
        private fun bloodlineName(clan: String, bloodline: String) = "character.clan.$clan.bloodline.$bloodline.name"
        private fun bloodlineDescription(clan: String, bloodline: String) =
            "character.clan.$clan.bloodline.$bloodline.description"
    }

    fun printKeys() {
        attributeGrades.forEach { println(it) }
        attributeGroups.forEach {
            println(it.key)
            it.attributes.forEach { attribute -> println(attribute.key) }
        }
        ClanGroup.entries.forEach { println(it.key) }
        clans.forEach {
            println(it.nameKey)
            println(it.descriptionKey)

            it.bloodlines.forEach { bloodline ->
                println(bloodline.nameKey)
                println(bloodline.descriptionKey)
            }
        }
    }
}