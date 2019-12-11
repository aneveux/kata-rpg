# Contributing Guide

- Contributing to this project is fairly easy. This document shows you how to get started

## General

Since that project mainly is a Code Kata exercise, its main target is just to provide some fun specifications for developers to play with. Contributions are welcome on all aspects of the project though, including the slides, the examples of source code, or either new implementation in new languages!

## Submitting changes

- Fork the repo
  - <https://github.com/aneveux/kata-rpg/fork>
- Check out a new branch based and name it to what you intend to do:
  - Example:
    ````
    $ git checkout -b BRANCH_NAME
    ````
    If you get an error, you may need to fetch fooBar first by using
    ````
    $ git remote update && git fetch
    ````
  - Use one branch per fix / feature
- Commit your changes
  - Please provide a git message that explains what you've done
  - Please make sure your commits follow the [conventions](https://gist.github.com/robertpainsi/b632364184e70900af4ab688decf6f53#file-commit-message-guidelines-md)
  - Commit to the forked repository
  - Example:
    ````
    $ git commit -am 'Add some fooBar'
    ````
- Push to the branch
  - Example:
    ````
    $ git push origin BRANCH_NAME
    ````
- Make a pull request
  - Make sure you send the PR to the <code>fooBar</code> branch

If you follow these instructions, your PR will land pretty safely in the main repo!
