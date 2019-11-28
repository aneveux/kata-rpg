package com.github.aneveux.kata.rpg.topdown

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class GameTest : StringSpec() {
    init {

        // First, let's introduce our top level elements, since we're using top down.
        // We'll just define the really basic entry points of our program.
        // Our test is basically testing nothing at this point, but it describes entry points.

        "Characters are fighting each others during a battle" {
            val wizard = Character(0, 0, 0, 0)
            val paladin = Character(0, 0, 0, 0)
            Battle(wizard, paladin)
        }

        // OK, let's move on, our characters need to fight now!
        // We'll continue with basic entries: we need some power and damages to compute the first part of the formula!
        // Let's put that in our model!

        "Characters may inflict damages to each others depending on their power" {
            val wizard = Character(power = 10, damages = 5, resistance = 0, hp = 0)
            wizard.attackDamage shouldBe 50
        }

        // We know how to calculate damage dealt, let's go on the other side and calculate how much damage is
        // received after reducing them with resistance!

        "Characters receiving damages are reducing them depending on their resistance" {
            // Notice how I'm using named parameters to give more insights about what I add in the code base
            val wizard = Character(power = 10, damages = 5, resistance = 2, hp = 0)
            wizard.reducedDamage(50) shouldBe 25
        }

        // Wait! There's something to clarify here! Since a division is coming, how about decimal numbers?
        // We'll write a test to define our rule! In our example, we'll go for rounded down numbers.

        "Damages received by the characters are rounded down" {
            val wizard = Character(10, 5, 2, 0)
            wizard.reducedDamage(9) shouldBe 4
        }

        // Hey! Another nice typical trap! What are we doing when resistance is 0? Dividing by 0 will break the code!
        // Time to ask for some clarification and write a test about that!
        // In our case, we'll just apply all the damages (for the example).

        "Characters with no resistance will receive all the damages dealt" {
            val wizard = Character(10, 5, 0, 0)
            wizard.reducedDamage(50) shouldBe 50
        }

        // Let's move on. Characters can produce and receive damages. Now, this should have an impact on their HPs!
        // Let's implement it right now.

        "Characters receiving damages will lose HPs accordingly" {
            val wizard = Character(10, 5, 2, 100)
            // Notice that we now have a decision to make about our API!
            // Should we have a method taking as an input the reduced damages, and only work on the HP,
            // Or should we have a method taking the raw damages and actually calling the reduction damage function?
            // We'll choose the first solution for this example to have only one responsibility per method
            //
            // And another clarification comes right after it!
            // Should we mutate our original object to lower its HP, or have immutable objects and return another one
            // with a correct amount of HPs?
            // We'll go for the immutable way in our example
            wizard.receiveDamage(20) shouldBe wizard.copy(hp = 80)
        }

        // Hey! Time for another clarification! What happens when a Character loses more HP than it has?
        // Will it go below 0? Let's write a test to state that.

        "Characters receiving more damages than their HP will end with 0 hp left" {
            val wizard = Character(10, 5, 2, 100)
            wizard.receiveDamage(2000) shouldBe wizard.copy(hp = 0)
        }

        // Note that at this point we may have other questions coming! Like how about a negative amount of damage?
        // Will it heal the Character? If so, will it heal the Character indefinitely, or does the Character have
        // a maximum and fixed amount of hp?
        // We won't deal with it in our example, but that is clearly a rule which needs an answer at some point.

        // At this point, we'd certainly want to start plugging all those things together to start simulating some
        // fights... And we see that we're missing some design!
        // We have a Battle object containing our 2 characters... So basically, we'd like to generate the rounds
        // during which the characters are dealing damages to each others!
        // Let's simply write a test to illustrate the design:

        "A battle should allow to generate the next round of the fight opposing the characters" {
            val wizard = Character(10, 5, 2, 100)
            val paladin = Character(10, 5, 2, 100)
            val battle = Battle(wizard, paladin)

            // And here we'll define our design. Basically, we'll have an object defining a Round, containing our
            // characters. Since at the beginning of the battle it's the first round, we'll just get a wrapper of those
            // characters.
            battle.nextRound() shouldBe Round(wizard, paladin)
        }

        // Notice how we wrote the bare minimum piece of code to make the test pass!
        // Now, we'll go a bit deeper. During a round, characters will actually inflict damages to each others.
        // What we'd like, is for a Round to be resolved by giving us the new state of the Characters.
        // Let's go with baby steps (which we can refactor later) to illustrate what we do:

        "The first step of the round resolution should apply the damages from the first player to the second" {
            val wizard = Character(10, 5, 2, 100)
            val paladin = Character(10, 5, 2, 100)
            // We'll directly create our Round object for the tests to concentrate on what we want to validate
            val round = Round(wizard, paladin)

            // For the first step of the resolution, basically, wizard will hit the paladin.
            // wizard will do 50 damages (cause 10power * 5damages)
            // paladin will reduce the damages to 25 because of it's resistance of 2
            // so paladin will be left with 75hps (cause it had 100 in the beginning)

            // This kind of test could be split up even more, but hey, we want to move a bit to illustrate things
            round.firstStepResolution() shouldBe Pair(wizard, paladin.copy(hp = 75))
        }

        // Nice! Let's go ahead and implement the second step!
        // Now, we need the exact opposite! Our second character needs to inflict damages to the first one.
        // Those are the same rules, so let's implement a test!

        "The second step of the round resolution should apply the damages from the second player to the second" {
            val wizard = Character(10, 5, 2, 100)
            val paladin = Character(10, 5, 2, 100)
            // We'll directly create our Round object for the tests to concentrate on what we want to validate
            val round = Round(wizard, paladin)

            // OK, now we expect the exact opposite thing.
            // Notice that (for our baby steps) the second step will not include the first step,
            // It'll just compute the other part of the job, and we'll need to aggregate both steps to come to the resolution!
            // Since we have the same values for our characters, the hps in the result will be the same
            round.secondStepResolution() shouldBe Pair(wizard.copy(hp = 75), paladin)
        }

        // Fine! At this point, you should clearly see that some refactoring should be done, and some code is a duplicated.
        // But let's continue a bit with our baby steps, just to finish the round resolution. We'll refactor the code
        // just after!

        "The resolution of a round should return the state of both characters after they applied damages to each others" {
            val wizard = Character(10, 5, 2, 100)
            val paladin = Character(10, 5, 2, 100)
            // We'll directly create our Round object for the tests to concentrate on what we want to validate
            val round = Round(wizard, paladin)

            // OK, this time, we want the full resolution:
            round.resolution() shouldBe Pair(wizard.copy(hp = 75), paladin.copy(hp = 75))
        }

        // Yes! It works!
        // Wait! I'm still in the baby steps, and I used the same values for the damages...
        // If I swapped things in my code, my tests wouldn't catch it!
        // Let's quickly fix that with just another test to be sure!

        "The resolution of a round should return the state of both characters after they applied the correct damages to each others" {
            val wizard = Character(10, 5, 2, 100)
            // I'll just change some values from the paladin
            val paladin = Character(4, 2, 1, 80)
            // We'll directly create our Round object for the tests to concentrate on what we want to validate
            val round = Round(wizard, paladin)

            // OK, this time, we want the full resolution:
            // let's calculate it by hand again:
            // wizard will deal 50 damages, paladin will take those 50 because it has 1 resistance, so paladin will end with 30 hp
            // paladin will deal 8 damages, wizard will take 4 damages because of its 2 resistances, so it'll end with 96hp
            round.resolution() shouldBe Pair(wizard.copy(hp = 96), paladin.copy(hp = 30))
        }

        // Time for a break!
        // You can tell me that the test data isn't the best, and I'd agree. But here I'm mostly using the tests to
        // design my application. I would definitely write more tests with better data to actually secure my code in a real life
        // application.
        // Also, at this point I'm getting confident about my source code, so I would do a bit of refactoring.
        // Which I can safely do... because I have tests!

        // For keeping the "history" of the tests and the way I wrote the application, I'll keep the tests and source code
        // I wrote earlier so you can follow my path.
        // Another cleaned package will be published in the repository with the actual cleaned up code.

        // Now that the refactoring is done, I'll keep going with the business logic. Basically, once a round is done,
        // I'd like to know its status. For this, I need to know if Characters are still alive... Let's code that!

        "A character with 0 HPs is considered dead, otherwise it is alive" {
            val wizard = Character(10, 5, 2, 100)
            wizard.isAlive shouldBe true
            wizard.receiveDamage(100).isAlive shouldBe false
        }

        // We know about our characters, so now we can get some information about the round itself!
        // A round will be considered final if at least one character dies.

        "A round is final if at least one character dies" {
            val wizard = Character(10, 5, 2, 0)
            val paladin = Character(10, 5, 2, 50)
            val finalRound = Round(wizard, paladin)

            finalRound.isFinal shouldBe true

            val nonFinalRound = Round(paladin, paladin)
            nonFinalRound.isFinal shouldBe false
        }

        // Hey! Time to decide what to do if both character die!
        // In a first place, we'd just say that we consider it as final round as well.
        "A round is final if both character die" {
            val paladin = Character(10, 5, 2, 0)
            val tieRound = Round(paladin, paladin)
            tieRound.isFinal shouldBe true
        }

        // We've got enough game logic to start assembling things now!
        // Basically, what I'd like is to simulate rounds until we find a final state.
        // The Battle looks like a good place to do so, so let's implement that in there.
        // First, I'd like to move the nextRound calculation which is right now in Battle
        // in the Round class, and actually implement it

        "The next round of a final round is itself" {
            val paladin = Character(10, 5, 2, 0)
            val finalRound = Round(paladin, paladin)
            finalRound.nextRound() shouldBe finalRound
        }

        "The next round of a non final round should be a round starting from the resolution of the current one" {
            val paladin = Character(10, 2, 1, 100)
            val round = Round(paladin, paladin)
            round.nextRound() shouldBe Round(paladin.receiveDamage(20), paladin.receiveDamage(20))
        }

        // Nice! nextRound doesn't have to be part of the Battle anymore!
        // Now, the Battle should actually contain all the rounds required for reaching to an end!
        // Let's add that in the battle!

        "A battle should contain the rounds required for the fight" {
            val paladin = Character(10, 2, 1, 100)
            val battle = Battle(paladin, paladin)
            battle.rounds shouldBe listOf()
        }

        // Let's continue with baby steps now. The battle should allow to go from state to state, by generating
        // rounds after rounds.
        // We'll try continuing with our immutable way of writing code and say that the next state of a battle
        // is a battle with one more round in it.

        "The next state of a new battle is a battle with one round stored" {
            val paladin = Character(10, 2, 1, 100)
            val battle = Battle(paladin, paladin)
            battle.nextState() shouldBe battle.copy(rounds = listOf(Round(paladin, paladin)))
        }

        // Next baby step, if there are some already calculated rounds, the next state of a battle should append those
        // rounds in the list

        "The next state of an ongoing battle is a battle with several rounds stored" {
            val paladin = Character(10, 2, 1, 100)
            val battle = Battle(paladin, paladin)
            battle.nextState() shouldBe battle.copy(rounds = listOf(Round(paladin, paladin)))
            battle.nextState().nextState() shouldBe battle.copy(
                rounds = listOf(
                    Round(paladin, paladin),
                    Round(paladin.receiveDamage(20), paladin.receiveDamage(20))
                )
            )
        }

        // One more baby step, the next state of a battle which reached a final round is the really same battle

        "The next state of a battle reaching a final round is the same battle" {
            val paladin = Character(10, 2, 1, 20)
            val battle = Battle(paladin, paladin)
            battle.nextState().nextState().nextState() shouldBe battle.copy(
                rounds = listOf(
                    Round(paladin, paladin),
                    Round(paladin.receiveDamage(20), paladin.receiveDamage(20))
                )
            )
        }

        // Fine! We've got ourselves a way to simulate the battle until it ends!
        // We clearly need some refactoring now. We implemented our baby steps, but we should really merge some things
        // together!
        // The tests would probably require some refactoring as well, but we'll do a cleanup afterwards.
        // We won't modify both the tests and the code at the same time.

    }
}