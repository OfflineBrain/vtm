package com.example.demo

class Character {
    val stats = HashMap<String, Int>()
    var strength: Int = 0

    fun strength(strength: Int) {
        this.strength = strength
    }

    fun getter(stat: String): Int? {
        return stats[stat]
    }


    fun setter(stat: String, value: Int) {
        stats[stat] = value
    }
}