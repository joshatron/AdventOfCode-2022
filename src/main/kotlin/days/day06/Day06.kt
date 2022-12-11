package io.joshatron.aoc2022.days.day06

import io.joshatron.aoc2022.readDayInput

fun day06Puzzle01(): String {
    val signal = readDayInput(6)[0]
    return (getIndexOfFirstXUniqueChars(signal, 4) + 4).toString()
}

fun day06Puzzle02(): String {
    val signal = readDayInput(6)[0]
    return (getIndexOfFirstXUniqueChars(signal, 14) + 14).toString()
}

private fun getIndexOfFirstXUniqueChars(signal: String, x: Int): Int {
    for (i in 0 until signal.length-x+1) {
        var allDifferent = true
        for (j in 0 until x) {
            for (k in j+1 until x) {
                if (signal[i+j] == signal[i+k]) {
                    allDifferent = false
                    break
                }
            }
            if (!allDifferent) {
                break
            }
        }
        if (allDifferent) {
            return i
        }
    }

    return -1
}