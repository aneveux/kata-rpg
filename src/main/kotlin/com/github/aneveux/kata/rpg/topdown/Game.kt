package com.github.aneveux.kata.rpg.topdown

import org.jetbrains.annotations.TestOnly

enum class CharacterType {
    PALADIN, ROGUE, WIZARD
}

fun multiplier(firstCharacterType: CharacterType, secondCharacterType: CharacterType) =
    when (firstCharacterType to secondCharacterType) {
        CharacterType.PALADIN to CharacterType.ROGUE -> 1.5
        CharacterType.ROGUE to CharacterType.WIZARD -> 1.5
        CharacterType.WIZARD to CharacterType.PALADIN -> 1.5
        else -> 1.0
    }

data class Character(
    val power: Int,
    val damages: Int,
    val resistance: Int,
    val hp: Int,
    val type: CharacterType,
    val myDamages: Character.() -> Int
) {
    val attackDamage
        get() = power * damages

    val isAlive
        get() = hp > 0

    fun amplifiedAttackDamages(multiplier: Double) = (attackDamage * multiplier).toInt()

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

    fun nextCompleteState() = if (rounds.isEmpty()) copy(
        rounds = listOf(
            Round(
                firstPlayer,
                secondPlayer
            )
        )
    ) else if (rounds.last().isFinal) this else copy(rounds = rounds.plusElement(rounds.last().nextCompleteRound()))

    fun isOver() = rounds.isNotEmpty() && rounds.last().isFinal

    fun solve(): Battle = if (isOver()) this else nextCompleteState().solve()

    enum class Result {
        TIE, ONGOING, VICTORY
    }

    sealed class Results {
        object NotStarted : Results()
        object Ongoing : Results()
        object Tie : Results()
        data class Victory(val winner: Character) : Results()
    }

    fun analyze(round: Round) = with(round) {
        when {
            !isFinal -> Results.Ongoing
            firstPlayer.isAlive -> Results.Victory(firstPlayer)
            secondPlayer.isAlive -> Results.Victory(secondPlayer)
            else -> Results.Tie
        }
    }

    fun results() = if (rounds.isEmpty()) Results.NotStarted else analyze(rounds.last())

    fun result() =
        if (isOver() && !rounds.last().firstPlayer.isAlive && !rounds.last().secondPlayer.isAlive) Result.TIE else if (isOver() && (rounds.last().firstPlayer.isAlive || rounds.last().secondPlayer.isAlive)) Result.VICTORY else Result.ONGOING
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

    private fun Character.multipliedHitBy(opponent: Character) = receiveDamage(
        reducedDamage(
            opponent.amplifiedAttackDamages(
                multiplier(opponent.type, this.type)
            )
        )
    )

    // For our baby steps example, I'll reuse the functions I wrote earlier.
    // You'll see that the result isn't the best looking code, so it'll obviously indicate that refactoring is needed!
    fun resolution() = firstPlayer.hitBy(secondPlayer) to secondPlayer.hitBy(firstPlayer)

    fun completeResolution() = firstPlayer.multipliedHitBy(secondPlayer) to secondPlayer.multipliedHitBy(firstPlayer)

    fun nextRound() = if (isFinal) this else with(this.resolution()) { Round(first, second) }
    fun nextCompleteRound() = if (isFinal) this else with(this.completeResolution()) { Round(first, second) }
}