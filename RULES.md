# ⛩ Code Kata - Simple RPG

The simple RPG game you're implementing allows to simulate the battle between 2 different characters to define the winner.

There are 3 types of characters: Paladins, Rogues, and Wizards. Paladins are stronger than Rogues, which are stronger than Wizards, which themselves are stronger than Paladins.

Defining who's winning a fight is really easy: characters only have one attack which deals a range of damage (for example, Rogues' attacks will deal from 5 to 8 damages). Those damages will be increased depending on the character's power, but lowered by the opponent's resistance.

The attacks are done simultaneously, which means equivalent characters may die together during the fight.

The formula we'll be using for the attack is the following one:

```
total_damage = (attack_damage * attacker_power) / defenser_resistance  
```

As we said earlier, some types of characters are stronger against other types, which means they'll deal 50% more damages against them.

For example, if a Wizard is attacking a Paladin, it'll deal:

```
total_damage = (attack_damage * attacker_power * 1.5) / defenser_resistance
```

Each attack is removing HPs to the defenser character, and the fight is over when at least one character dies. If one character remains alive, it's the winner of the fight.

## New need! Some additional rules!

It's time to get the benefits of your clean code! Here are some new rules to implement!

Characters now have special abilities depending on their types! They may cast spells which are helping them during their fights!

Wizard have a 20% chance to heal 10% of their HPs each time they attack.

Paladins have a 20% chance to double their resistance each time they defend.

Rogues have a 20% chance to double their damages each time they attack.