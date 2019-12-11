<div align="center">
 <img width=200px height=200px src="img/icon.png" alt="RPG Simple Kata" />
</div>

<h3 align="center">Code Kata - Simple RPG</h3>

<div align="center">

  [![Status](https://img.shields.io/badge/status-active-success.svg)]() 
  [![GitHub Issues](https://img.shields.io/github/issues/aneveux/kata-rpg.svg)](https://github.com/aneveux/kata-rpg/issues)
  [![GitHub Pull Requests](https://img.shields.io/github/issues-pr/aneveux/kata-rpg.svg)](https://github.com/aneveux/kata-rpg/pulls)
  [![License](https://img.shields.io/badge/license-MIT-blue.svg)](/LICENSE)

</div>

---

<p align="center">
This <i>Simple RPG</i> project is a <a href="https://en.wikipedia.org/wiki/Kata_(programming)">Code Kata</a> exercise one can use to experiment different coding techniques. It is provided with a few slides about <a href="https://en.wikipedia.org/wiki/Extreme_Programming">eXtreme Programming</a> to promote TDD practices.
</p>

## 📝 Table of Contents
- [About](#about)
- [Getting Started](#getting_started)
- [Deployment](#deployment)
- [Built Using](#built_using)
- [TODO](TODO.md)
- [Contributing](CONTRIBUTING.md)
- [Authors](#authors)
- [Acknowledgments](#acknowledgement)

## 🧐 About <a name = "about"></a>

This *Code Kata* exercise is just a small specification for a program one could write to experiment different coding techniques. It's a rather simple and fun program to write, but the specification has some intended pitfalls so developers have to take a couple decisions along the exercise to complete their implementation.

This whole project has been designed and used for a presentation of some *eXtreme Programming* techniques, with a particular focus on *Test Driven Development*. It is provided with a [slide deck](https://aneveux.github.io/kata-rpg/#/) which has been used to present either *eXtreme Programming* and the results of the coding exercise.

On top of that, you'll find in that repository a *Kotlin* project containing various implementations of the problem: a completely naive solution, with no tests written, which is just basically trying to make the program work as fast as possible; a top down TDD implementation, with comments and step by step approach (with no refactoring to try to keep the evolution of the code); and finally, a refactored version of the top down TDD approach.

## 🏁 Getting Started <a name = "getting_started"></a>

If you'd like to practice your development techniques, feel free to have a look at the [specifications](RULES.md) written for the code kata, choose your favourite programming language, and go for it! There's nothing more than just experimenting and getting better at programming!

If you'd like to reuse the presentation about eXtreme Programming, a published and packaged version is available in the `docs/` folder: simply open the `index.html` file with your favourite browser, and you should be good to go.

If you'd like to play a bit with Kotlin, either writting your own program or modifying the one we wrote, you can simply import the project in your IDE (it is a Maven project, so all dependencies should be retrieved for your if you have Maven installed).

If you'd like to follow the development process I followed while solving the exercise, feel free to read the comments in [that test file](src/test/kotlin/com/github/aneveux/kata/rpg/topdown/GameTest.kt).

### Prerequisites

If you want to play with our Kotlin implementation, we recommend you to have:

- [OpenJDK](https://openjdk.java.net/)
- [Maven](https://maven.apache.org/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) - *the Community edition is fine*

And you shouldn't need more than this.

Otherwise, you can go ahead and write some code with your favourite language and tools.

## 🔧 Running the tests <a name = "tests"></a>

Unit Tests are provided for the top down TDD implementation we wrote in Kotlin. You can execute them using maven with the following command: `mvn test`, or directly from your IDE.

## 🚀 Deployment <a name = "deployment"></a>

If you'd like to modify the slides and generate a new version out of them, you'll find the source code from the slides in `slides.md` (which is just markdown) and you can generate slides out of that file easily by using [reveal-md](https://github.com/webpro/reveal-md).

Simply run `reveal-md slides.md --static docs --static-dirs img` or execute the `publish.sh` shell script provided in that repository.

## ⛏️ Built Using <a name = "built_using"></a>

Our implementation is written with:

- [Kotlin](https://kotlinlang.org/)
- [KotlinTest](https://github.com/kotlintest/kotlintest)

Our slides are written with:

- [reveal-md](https://github.com/webpro/reveal-md)

## ✍️ Authors <a name = "authors"></a>

- [@aneveux](https://github.com/aneveux)
- [@jmdesprez](https://github.com/jmdesprez)

See also the list of [contributors](https://github.com/aneveux/kata-rpg/contributors) who participated in this project.

## 🎉 Acknowledgements <a name = "acknowledgement"></a>

Thanks a lot to all those projects we used at some point:

- [RevealJS](https://revealjs.com/#/)
- [reveal-md](https://github.com/webpro/reveal-md)
- [KotlinTest](https://github.com/kotlintest/kotlintest)
- [The Documentation Compendium](https://github.com/kylelobo/The-Documentation-Compendium/)