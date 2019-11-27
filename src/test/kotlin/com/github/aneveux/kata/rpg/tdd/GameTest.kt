package com.github.aneveux.kata.rpg.tdd

import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec


class GameTest : StringSpec() {
    init {

        "An attack damage is the multiplication of the damages, the power, and a multiplier" {
            attack(5, 5, 2) shouldBe 50
        }

        // Questions: can we have negative numbers? how about 0?
        // Are negative numbers healing the opponent?

        // Bonus: Property Based Testing like

        "An attack damage is always the multiplication of the damages, the power, and a multiplier" {
            assertAll { damage: Int, power: Int, multiplier: Int ->
                attack(damage, power, multiplier) shouldBe damage * power * multiplier
            }
        }

        "The total damages of an attack is reduced by the defender resistance" {
            damage(50, 2) shouldBe 25
        }

        // Questions: how about floating numbers?
        // Choice for our simple version: it'll be truncated Integers, but let's write a test to clarify it.

        "The total damages of an odd attack is a floor rounded number" {
            damage(25, 2) shouldBe 12
        }

        "A hit removes as much HPs as the damages it inflicts" {
            hit(50, 30) shouldBe 20
        }

        // Questions: do we return negative numbers or block at 0?

        // Note: yes, we write tests for simple functions, but it allows to clearly describe inputs and outputs,
        // and allow the evolution of the code.
        // It allows us to ask good questions and take correct decisions that will impact the rest of the project.

        // Break: are we still OK with our code's organization or is some refactoring needed?
        // As per now, we're OK, let's continue.

        "Characters have HPs, power, resistance, and can deal attack damages" {
            val character = Character(100, 100, 100, 5)
        }

        // Note: this test will be removed at some point, it is written in the TDD process to lead to the object creation
        // It'll disappear when we'll have better tests

        "A character hitting another character will generate a certain amount of damages" {
            val attacker = Character(100, 10, 5, 2)
            val defender = Character(100, 10, 5, 2)

            val defenderRemainingLife = computeRemainingLife(attacker, defender)

            // 96 = 100hp - ( 10power * 2damages / 5resistance)
            defenderRemainingLife shouldBe 96
        }

        // Note: at this point it's interesting to wonder if it couldn't be interesting to move that new method
        // in the Character class, because it uses most of its attributes. Again one decision to take.

        // Because we're in Kotlin, it would be a nice candidate for an extension!

        "Attacks are simultaneous: attacker and defender are dealing damages to each others at the same time" {
            val attacker = Character(100, 10, 5, 2)
            val defender = Character(50, 10, 5, 2)

            val (attackerUpdated, defenderUpdated) = punch(attacker, defender)

            attackerUpdated shouldBe attacker.copy(hp = 96)
            defenderUpdated shouldBe defender.copy(hp = 46)
        }

        // Note: at this point, we can discuss wether or not we should have a dedicated object containing the attacker and
        // defender. Something like a round, or a match, or battle, or anything similar.

        // Note: It could be a good time to do some refactoring as well.

        // To continue, we'll start implementing higher game mechanics, using KISS.
        // So we always try to find the easiest case to deal with.

        "A fight is a tie if both characters are dead" {
            val isDead = true

            isTie(isDead, isDead) shouldBe true
        }

        "A character is dead if its HP are 0 or less" {
            isDead(0) shouldBe true
            isDead(-1000) shouldBe true
        }

        // Note: this should be a Kotlin extension on the Character object, some refactoring is needed.
        // But again for TDD illustration, we just KISS.

        "A fight has a winner if only one of the characters is dead" {
            val isDead = true
            val isNotDead = false

            hasWinner(isDead, isDead) shouldBe false
            hasWinner(isNotDead, isNotDead) shouldBe false
            hasWinner(isNotDead, isDead) shouldBe true
            hasWinner(isDead, isNotDead) shouldBe true
        }

        "A fight is ongoing if both characters are alive" {
            val isDead = true
            val isNotDead = false

            isFightOngoing(isDead, isDead) shouldBe false
            isFightOngoing(isNotDead, isNotDead) shouldBe true
            isFightOngoing(isNotDead, isDead) shouldBe false
            isFightOngoing(isDead, isNotDead) shouldBe false
        }

        // Note: we probably have duplicate code, and some refactoring would be needed now.
        // We would require something to know the winner, but we saw there's something about tie.

        // Note: at this point, it's really interesting to see that we have lots of questions about the design.
        // How to represent the various states of a game (tie, over, winner, etc.)
        // Some refactoring for design purpose would be needed.

    }
}
