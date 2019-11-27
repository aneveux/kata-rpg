package com.github.aneveux.kata.rpg.topdown

data class Character(val power: Int, val damages: Int, val resistance: Int, val hp: Int) {
    val attackDamage
        get() = power * damages

    fun reducedDamage(damage: Int) = if (resistance == 0) damage else damage / resistance

    fun receiveDamage(damage: Int) = copy(hp = if (damage >= hp) 0 else hp - damage)
}

class Battle(val firstPlayer: Character, val secondPlayer: Character) {
    fun nextRound() = Round(firstPlayer, secondPlayer)
}

data class Round(val firstPlayer: Character, val secondPlayer: Character) {
    fun firstStepResolution() = firstPlayer to with(secondPlayer) {
        receiveDamage(reducedDamage(firstPlayer.attackDamage))
    }

    fun secondStepResolution() = with(firstPlayer) {
        receiveDamage(reducedDamage(secondPlayer.attackDamage)) to secondPlayer
    }

    // For our baby steps example, I'll reuse the functions I wrote earlier.
    // You'll see that the result isn't the best looking code, so it'll obviously indicate that refactoring is needed!
    fun resolution() = secondStepResolution().first to firstStepResolution().second
}