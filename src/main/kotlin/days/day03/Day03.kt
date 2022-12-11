package io.joshatron.aoc2022.days.day03

import io.joshatron.aoc2022.readDayInput

fun day03Puzzle01(): String {
    return parseInputToRucksacks(readDayInput(3))
        .fold(0) { acc, cur -> acc + priorityOfChar(findCommonChar(cur.compartment1, cur.compartment2)) }
        .toString()
}

private fun parseInputToRucksacks(input: List<String>): List<Rucksack> {
    return input.map { Rucksack(it.substring(0, it.length/2), it.substring(it.length/2)) }
}

private class Rucksack(val compartment1: String, val compartment2: String)

private fun findCommonChar(str1: String, str2: String): Char {
    for (c1 in str1.asSequence()) {
        for (c2 in str2.asSequence()) {
            if (c1 == c2) {
                return c1
            }
        }
    }
    return str1[0]
}

private fun priorityOfChar(c: Char): Int {
    return if (c.isLowerCase()) {
        c.code - 96
    } else {
        c.code - 64 + 26
    }
}

fun day03Puzzle02(): String {
    val rucksacks = parseInputToRucksacks(readDayInput(3))
    var total = 0
    for (i in 0 until rucksacks.size/3) {
        total += priorityOfChar(findCommonCharToRucksacks(rucksacks[i*3], rucksacks[i*3+1], rucksacks[i*3+2]))
    }

    return total.toString()
}

private fun findCommonCharToRucksacks(r1: Rucksack, r2: Rucksack, r3: Rucksack): Char {
    for (c1 in "${r1.compartment1}${r1.compartment2}".asSequence()) {
        for (c2 in "${r2.compartment1}${r2.compartment2}".asSequence()) {
            for (c3 in "${r3.compartment1}${r3.compartment2}".asSequence()) {
                if (c1 == c2 && c2 == c3) {
                    return c1
                }
            }
        }
    }
    return r1.compartment1[0]
}