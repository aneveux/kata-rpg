package com.github.aneveux.kata.rpg.tdd

fun attack(damage: Int, power: Int, multiplier: Int) = damage * power * multiplier

fun damage(attackDamage: Int, resistance: Int) = attackDamage / resistance

fun hit(hp: Int, damages: Int) = hp - damages

data class Character(val hp: Int, val power: Int, val resistance: Int, val damages: Int)

fun computeRemainingLife(attacker: Character, defender: Character) =
    hit(defender.hp, damage(attack(attacker.damages, attacker.power, 1), defender.resistance))

fun punch(playerA: Character, playerB: Character) = Pair(
    playerA.copy(hp = computeRemainingLife(playerB, playerA)),
    playerB.copy(hp = computeRemainingLife(playerA, playerB))
)

fun isTie(isPlayerADead: Boolean, isPlayerBDead: Boolean) = isPlayerADead && isPlayerBDead

fun isDead(hp: Int) = hp <= 0

fun hasWinner(isPlayerADead: Boolean, isPlayerBDead: Boolean) = isPlayerADead xor isPlayerBDead

fun isFightOngoing(isPlayerADead: Boolean, isPlayerBDead: Boolean) = !isPlayerADead and !isPlayerBDead