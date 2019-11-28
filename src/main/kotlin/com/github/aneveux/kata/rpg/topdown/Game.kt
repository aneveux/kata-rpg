package com.github.aneveux.kata.rpg.topdown

import org.jetbrains.annotations.TestOnly

data class Character(val power: Int, val damages: Int, val resistance: Int, val hp: Int) {
    val attackDamage
        get() = power * damages

    val isAlive
        get() = hp > 0

    fun reducedDamage(damage: Int) = if (resistance == 0) damage else damage / resistance

    fun receiveDamage(damage: Int) = copy(hp = if (damage >= hp) 0 else hp - damage)
}

data class Battle(val firstPlayer: Character, val secondPlayer: Character, val rounds: List<Round> = listOf()) {
    @TestOnly
    fun nextRound() = Round(firstPlayer, secondPlayer)

    fun nextState() = if (rounds.isEmpty()) copy(
        rounds = listOf(
            Round(
                firstPlayer,
                secondPlayer
            )
        )
    ) else if (rounds.last().isFinal) this else copy(rounds = rounds.plusElement(rounds.last().nextRound()))
}

data class Round(val firstPlayer: Character, val secondPlayer: Character) {
    val isFinal
        get() = !(firstPlayer.isAlive && secondPlayer.isAlive)

    @TestOnly
    fun firstStepResolution() = firstPlayer to with(secondPlayer) {
        receiveDamage(reducedDamage(firstPlayer.attackDamage))
    }

    @TestOnly
    fun secondStepResolution() = with(firstPlayer) {
        receiveDamage(reducedDamage(secondPlayer.attackDamage)) to secondPlayer
    }

    // During refactoring time, I'll simply use some of Kotlin features to have clean and readable code
    // allowing me to have a simple function describing a character being hit by another one
    private fun Character.hitBy(opponent: Character) = receiveDamage(reducedDamage(opponent.attackDamage))

    // For our baby steps example, I'll reuse the functions I wrote earlier.
    // You'll see that the result isn't the best looking code, so it'll obviously indicate that refactoring is needed!
    fun resolution() = firstPlayer.hitBy(secondPlayer) to secondPlayer.hitBy(firstPlayer)

    fun nextRound() = if (isFinal) this else with(this.resolution()) { Round(first, second) }
}