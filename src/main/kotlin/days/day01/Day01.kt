package io.joshatron.aoc2022.days.day01

import io.joshatron.aoc2022.readDayInput

fun day01Puzzle01(): String {
    val elves = parseElfAmounts(readDayInput(1))
    return elves.reduce { acc, next -> if (acc.total > next.total)  acc else next }.total.toString()
}

fun day01Puzzle02(): String {
    val elves = parseElfAmounts(readDayInput(1))
    var top1Elf = Elf(ArrayList())
    var top2Elf = Elf(ArrayList())
    var top3Elf = Elf(ArrayList())

    elves.forEach { elf -> if (elf.total > top1Elf.total) {
        top3Elf = top2Elf
        top2Elf = top1Elf
        top1Elf = elf
    } else if (elf.total > top2Elf.total) {
        top3Elf = top2Elf
        top2Elf = elf
    } else if (elf.total > top3Elf.total) {
        top3Elf = elf
    }
    }

    return (top1Elf.total + top2Elf.total + top3Elf.total).toString()
}

private fun parseElfAmounts(input: List<String>): List<Elf> {
    val elves: MutableList<Elf> = ArrayList()

    var currentFoodItems: MutableList<Int> = ArrayList()
    for (line in input) {
        if (line.isBlank()) {
            elves.add(Elf(currentFoodItems))
            currentFoodItems = ArrayList()
        } else {
            currentFoodItems.add(line.toInt())
        }
    }

    elves.add(Elf(currentFoodItems))

    return elves
}

private class Elf(foodItems: List<Int>) {
    val total = foodItems.fold(0){ acc, next -> acc + next }
}