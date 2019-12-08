package com.github.aneveux.kata.rpg.refactoring

enum class CharacterType {
    PALADIN, ROGUE, WIZARD;

    fun multiplier(opponent: CharacterType) = when (this to opponent) {
        PALADIN to ROGUE -> 1.5
        ROGUE to WIZARD -> 1.5
        WIZARD to PALADIN -> 1.5
        else -> 1.0
    }
}

data class Character(
    val power: Int, val damages: IntRange, val resistance: Int, val hp: Int, val type: CharacterType,
    val damagePicker: Character.() -> Int = { damages.random() }
) {

    val isAlive
        get() = hp > 0

    fun attackDamages(multiplier: Double) = (power * damagePicker() * multiplier).toInt()

    fun reduceDamages(damage: Int) = if (resistance == 0) damage else damage / resistance

    fun receiveDamages(damage: Int) = copy(hp = if (damage >= hp) 0 else hp - damage)
}

data class Battle(val firstPlayer: Character, val secondPlayer: Character, val rounds: List<Round> = listOf()) {

    fun nextState() = when {
        rounds.isEmpty() -> copy(rounds = listOf(Round(firstPlayer, secondPlayer)))
        rounds.last().isFinal -> this
        else -> copy(rounds = rounds.plusElement(rounds.last().next()))
    }

    fun isOver() = rounds.isNotEmpty() && rounds.last().isFinal

    fun solve(): Battle = if (isOver()) this else nextState().solve()

    sealed class Results {
        object NotStarted : Results()
        object Ongoing : Results()
        object Tie : Results()
        data class Victory(val winner: Character) : Results()
    }

    fun results() = if (rounds.isEmpty()) Results.NotStarted else rounds.last().analyze()
}

data class Round(val firstPlayer: Character, val secondPlayer: Character) {
    val isFinal
        get() = !(firstPlayer.isAlive && secondPlayer.isAlive)

    fun analyze() = when {
        !isFinal -> Battle.Results.Ongoing
        firstPlayer.isAlive -> Battle.Results.Victory(firstPlayer)
        secondPlayer.isAlive -> Battle.Results.Victory(secondPlayer)
        else -> Battle.Results.Tie
    }

    private fun Character.hitBy(opponent: Character) = receiveDamages(
        reduceDamages(
            opponent.attackDamages(opponent.type.multiplier(this.type))
        )
    )

    fun resolution() = Round(firstPlayer.hitBy(secondPlayer), secondPlayer.hitBy(firstPlayer))

    fun next() = if (isFinal) this else resolution()
}