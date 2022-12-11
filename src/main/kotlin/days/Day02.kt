package io.joshatron.aoc2022.day

import io.joshatron.aoc2022.readDayInput

fun day02Puzzle01(): String {
    return addRoundsScore(parseRoundsPuzzle01(readDayInput(2))).toString()
}

private fun addRoundsScore(rounds: List<Round>) = rounds.fold(0) { total, round -> total + round.player2Score }

private fun parseRoundsPuzzle01(input: List<String>): List<Round> = input.map { Round(firstLetterToChoice(it[0]), puzzle01SecondLetterToChoice(it[2])) }

private fun firstLetterToChoice(letter: Char) = when (letter) {
    'A' -> Choice.ROCK
    'B' -> Choice.PAPER
    'C' -> Choice.SCISSORS
    else -> Choice.UNKNOWN
}

private fun puzzle01SecondLetterToChoice(letter: Char) = when (letter) {
    'X' -> Choice.ROCK
    'Y' -> Choice.PAPER
    'Z' -> Choice.SCISSORS
    else -> Choice.UNKNOWN
}

private class Round(player1Choice: Choice, player2Choice: Choice) {
    val player2Score = score(player2Choice, player1Choice)
}

private enum class Choice {
    ROCK,
    PAPER,
    SCISSORS,
    UNKNOWN,
}

private fun score(scoredPlayer: Choice, otherPlayer: Choice): Int {
    return when {
        scoredPlayer == Choice.ROCK && otherPlayer == Choice.ROCK -> 4
        scoredPlayer == Choice.ROCK && otherPlayer == Choice.PAPER -> 1
        scoredPlayer == Choice.ROCK && otherPlayer == Choice.SCISSORS -> 7
        scoredPlayer == Choice.PAPER && otherPlayer == Choice.ROCK -> 8
        scoredPlayer == Choice.PAPER && otherPlayer == Choice.PAPER -> 5
        scoredPlayer == Choice.PAPER && otherPlayer == Choice.SCISSORS -> 2
        scoredPlayer == Choice.SCISSORS && otherPlayer == Choice.ROCK -> 3
        scoredPlayer == Choice.SCISSORS && otherPlayer == Choice.PAPER -> 9
        scoredPlayer == Choice.SCISSORS && otherPlayer == Choice.SCISSORS -> 6
        else -> 0
    }
}

fun day02Puzzle02(): String {
    return addRoundsScore(parseRoundsPuzzle02(readDayInput(2))).toString()
}

private fun parseRoundsPuzzle02(input: List<String>): List<Round> = input.map {
    Round(firstLetterToChoice(it[0]), puzzle02SecondLetterToChoice(it[2], firstLetterToChoice(it[0])))
}

private fun puzzle02SecondLetterToChoice(letter: Char, firstChoice: Choice) = when  {
    letter == 'X' && firstChoice == Choice.ROCK -> Choice.SCISSORS
    letter == 'X' && firstChoice == Choice.PAPER -> Choice.ROCK
    letter == 'X' && firstChoice == Choice.SCISSORS -> Choice.PAPER
    letter == 'Y' && firstChoice == Choice.ROCK -> Choice.ROCK
    letter == 'Y' && firstChoice == Choice.PAPER -> Choice.PAPER
    letter == 'Y' && firstChoice == Choice.SCISSORS -> Choice.SCISSORS
    letter == 'Z' && firstChoice == Choice.ROCK -> Choice.PAPER
    letter == 'Z' && firstChoice == Choice.PAPER -> Choice.SCISSORS
    letter == 'Z' && firstChoice == Choice.SCISSORS -> Choice.ROCK
    else -> Choice.UNKNOWN
}
