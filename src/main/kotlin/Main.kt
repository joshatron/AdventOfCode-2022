import java.io.File

fun main(args: Array<String>) {
    println("Day 01, Puzzle 01: ${day01Puzzle01()}")
    println("Day 01, Puzzle 02: ${day01Puzzle02()}")
}

fun day01Puzzle01(): String {
    val elves = day01ParseElfAmounts(readDayInput(1))
    return elves.reduce { acc, next -> if (acc.total > next.total)  acc else next }.total.toString()
}

fun day01Puzzle02(): String {
    val elves = day01ParseElfAmounts(readDayInput(1))
    var top1Elf = Day01Elf(ArrayList())
    var top2Elf = Day01Elf(ArrayList())
    var top3Elf = Day01Elf(ArrayList())

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

fun day01ParseElfAmounts(input: List<String>): List<Day01Elf> {
    var elves: MutableList<Day01Elf> = ArrayList()

    var currentFoodItems: MutableList<Int> = ArrayList()
    for (line in input) {
        if (line.isBlank()) {
            elves.add(Day01Elf(currentFoodItems))
            currentFoodItems = ArrayList()
        } else {
            currentFoodItems.add(line.toInt())
        }
    }

    elves.add(Day01Elf(currentFoodItems))

    return elves
}

class Day01Elf(foodItems: List<Int>) {
    val total = foodItems.fold(0){ acc, next -> acc + next }
}

fun readDayInput(day: Int): List<String> =
    object {}::class.java.getResource(getFileName(day))?.readText(Charsets.UTF_8)?.split("\n") ?: ArrayList()

fun getFileName(day: Int): String {
    return "day0$day.txt"
}