package com.github.aneveux.kata.rpg.naive

abstract class Character(var hp: Int, val power: Int, val resistance: Int, private val damages: IntRange) {
    fun isAlive() = hp > 0
    fun attackDamage() = damages.random()
    abstract fun attackMultiplier(c: Character): Double
}

class Paladin(hp: Int, power: Int, resistance: Int, damages: IntRange) : Character(hp, power, resistance, damages) {
    override fun attackMultiplier(c: Character): Double {
        if (c is Rogue)
            return 1.5
        else
            return 1.0
    }
}

class Wizard(hp: Int, power: Int, resistance: Int, damages: IntRange) : Character(hp, power, resistance, damages) {
    override fun attackMultiplier(c: Character): Double {
        if (c is Paladin)
            return 1.5
        else
            return 1.0
    }
}

class Rogue(hp: Int, power: Int, resistance: Int, damages: IntRange) : Character(hp, power, resistance, damages) {
    override fun attackMultiplier(c: Character): Double {
        if (c is Wizard)
            return 1.5
        else
            return 1.0
    }
}

fun fight(first: Character, second: Character): Character? {
    println("Fight!")
    var turn = 0
    while (first.isAlive() && second.isAlive()) {
        println("ROUND ${turn++}")
        val totalDamageFirst = (first.attackDamage() * first.power * first.attackMultiplier(second)) / second.resistance
        val totalDamageSecond =
            (second.attackDamage() * second.power * second.attackMultiplier(first)) / first.resistance
        first.hp -= totalDamageSecond.toInt()
        second.hp -= totalDamageFirst.toInt()
    }
    if (first.isAlive())
        return first
    else if (second.isAlive())
        return second
    else return null
}

fun main() {
    val Lancelot = Paladin(100, 5, 8, (10..15))
    val Merlin = Wizard(90, 10, 6, (8..18))

    val winner = fight(Lancelot, Merlin)

    println("Winner: $winner")
}