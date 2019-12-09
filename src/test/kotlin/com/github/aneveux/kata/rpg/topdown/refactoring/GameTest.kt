package com.github.aneveux.kata.rpg.topdown.refactoring

import com.github.aneveux.kata.rpg.topdown.refactoring.Battle.Results.*
import com.github.aneveux.kata.rpg.topdown.refactoring.CharacterType.*
import io.kotlintest.matchers.numerics.shouldBeGreaterThan
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class GameTest : StringSpec() {
    init {

        // All test data is factorized here. Tests have led to defining the complete data structure.

        val dead = Character(0, (0..0), 0, 0, PALADIN)
        val alive = Character(0, (0..0), 0, 100, PALADIN)
        val paladin = Character(10, (2..5), 1, 100, PALADIN) { 2 }
        val wizard = Character(10, (2..5), 1, 100, WIZARD) { 2 }
        val resistant = Character(10, (2..5), 2, 100, PALADIN) { 2 }
        val weak = Character(10, (2..5), 0, 100, PALADIN) { 2 }
        val oneshot = Character(10, (2..5), 1, 20, PALADIN) { 2 }

        "Characters can fight each others during a Battle" {
            Battle(wizard, paladin)
        }

        "Damages dealt by the characters are amplified by their power" {
            paladin.attackDamages(1.0) shouldBe 20
        }

        "Damages received by the characters are reduced by their resistance" {
            resistant.reduceDamages(50) shouldBe 25
        }

        "Damages received by the characters are rounded down by the resistance" {
            resistant.reduceDamages(9) shouldBe 4
        }

        "Damages are not reduced at all when the character has no resistance at all" {
            weak.reduceDamages(50) shouldBe 50
        }

        "Applying damages to a Character will give you a Character with less HPs" {
            paladin.receiveDamages(20) shouldBe paladin.copy(hp = 80)
        }

        "Characters receiving more damages than their HP will end with 0 hp left" {
            paladin.receiveDamages(2000) shouldBe paladin.copy(hp = 0)
        }

        "The resolution of a Round returns both Characters with damages applied to each others" {
            val round = Round(paladin, paladin)
            round.resolution() shouldBe Round(paladin.copy(hp = 80), paladin.copy(hp = 80))
        }

        "The resolution of a Round should ensure damages are applied to the correct Characters" {
            val round = Round(paladin, alive)
            round.resolution() shouldBe Round(paladin.copy(hp = 100), alive.copy(hp = 80))
        }

        "A character with 0 HPs is considered dead, otherwise it is alive" {
            dead.isAlive shouldBe false
            alive.isAlive shouldBe true
            alive.receiveDamages(1000).isAlive shouldBe false
        }

        "A round is final if at least one character dies" {
            Round(dead, alive).isFinal shouldBe true
            Round(alive, dead).isFinal shouldBe true
            Round(alive, alive).isFinal shouldBe false
        }

        "A round is final if both characters die" {
            Round(dead, dead).isFinal shouldBe true
        }

        "The next round of a final round is itself" {
            val finalRound = Round(dead, dead)
            finalRound.next() shouldBe finalRound
        }

        "The next round of a non-final round contains the result of the previous round resolution" {
            val round = Round(paladin, paladin)
            round.next() shouldBe Round(paladin.receiveDamages(20), paladin.receiveDamages(20))
        }

        "The second state of a new Battle is a Battle containing one round" {
            val battle = Battle(paladin, paladin)
            battle.nextState() shouldBe battle.copy(rounds = listOf(Round(paladin, paladin)))
        }

        "The next state of an ongoing battle is a battle with an additional round stored" {
            val battle = Battle(paladin, paladin)
            battle.nextState() shouldBe battle.copy(rounds = listOf(Round(paladin, paladin)))
            battle.nextState().nextState() shouldBe battle.copy(
                rounds = listOf(
                    Round(paladin, paladin),
                    Round(paladin.receiveDamages(20), paladin.receiveDamages(20))
                )
            )
        }

        "The next state of a battle reaching a final round is the same battle" {
            val battle = Battle(oneshot, oneshot)
            battle.nextState().nextState().nextState() shouldBe battle.copy(
                rounds = listOf(
                    Round(oneshot, oneshot),
                    Round(oneshot.receiveDamages(20), oneshot.receiveDamages(20))
                )
            )
        }

        "A battle is over when its last round is a final one" {
            val battle = Battle(dead, dead)
            battle.isOver() shouldBe false
            battle.nextState().isOver() shouldBe true
        }

        "The result of a finished battle with 2 dead characters is a tie" {
            val battle = Battle(dead, dead)
            battle.nextState().results() shouldBe Tie
        }

        "The result of a finished battle with only 1 dead character is a victory" {
            val battle = Battle(dead, alive)
            battle.nextState().results() shouldBe Victory(alive)
        }

        "If no characters are dead yet, the results of a battle should be ongoing" {
            val battle = Battle(alive, alive)
            battle.nextState().results() shouldBe Ongoing
        }

        "If no rounds are generated yet, the results of a battle should be not started" {
            val battle = Battle(alive, alive)
            battle.results() shouldBe NotStarted
        }

        "Paladins deal 50% more damages to Rogues" {
            PALADIN.multiplier(ROGUE) shouldBe 1.5
        }

        "Rogues deal 50% more damages to Wizards" {
            ROGUE.multiplier(WIZARD) shouldBe 1.5
        }

        "Wizards deal 50% more damages to Paladins" {
            WIZARD.multiplier(PALADIN) shouldBe 1.5
        }

        "Other combinations of characters type deal no additional damages" {
            ROGUE.multiplier(PALADIN) shouldBe 1.0
            PALADIN.multiplier(WIZARD) shouldBe 1.0
            WIZARD.multiplier(ROGUE) shouldBe 1.0
        }

        "Characters with advantages on another type should deal more damages to them" {
            val paladinDamages = paladin.attackDamages(PALADIN.multiplier(WIZARD))
            val wizardDamages = wizard.attackDamages(WIZARD.multiplier(PALADIN))
            wizardDamages shouldBeGreaterThan paladinDamages
            wizardDamages shouldBe 30
        }

        "Damage amplification between different characters' type is happening before damage reduction phase" {
            val round = Round(wizard, paladin)
            round.resolution() shouldBe Round(wizard.copy(hp = 80), paladin.copy(hp = 70))
        }

        "Solving a battle allows to directly reach its final state" {
            val battle = Battle(paladin, wizard)

            battle.solve().isOver() shouldBe true
            battle.solve().results() shouldBe Victory(wizard.copy(hp = 20))
            battle.solve().rounds.size shouldBe 5
        }
    }
}