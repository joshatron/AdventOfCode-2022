package io.joshatron.aoc2022.days.day11

import io.joshatron.aoc2022.readDayInput
import java.util.*
import kotlin.collections.ArrayList

fun day11Puzzle01(): String {
    val monkeys = parseMonkeys(readDayInput(11))
    for (i in 0 until 20) {
        for (monkey in monkeys) {
            monkey.updateWorryPuzzle01()
            while (monkey.items.isNotEmpty()) {
                val item = monkey.items.remove()
                if (item % monkey.testDivisibleBy == 0L) {
                    monkeys[monkey.testTrueMonkey].items.add(item)
                } else {
                    monkeys[monkey.testFalseMonkey].items.add(item)
                }
            }
        }
    }

    return determineMonkeyBusiness(monkeys).toString()
}

private fun determineMonkeyBusiness(monkeys: List<Monkey>): Long {
    var top1Inspection: Long = 0
    var top2Inspection: Long = 0

    for (monkey in monkeys) {
        if (monkey.inspections > top1Inspection) {
            top2Inspection = top1Inspection
            top1Inspection = monkey.inspections.toLong()
        } else if (monkey.inspections > top2Inspection) {
            top2Inspection = monkey.inspections.toLong()
        }
    }

    return top1Inspection * top2Inspection
}

private fun parseMonkeys(input: List<String>): List<Monkey> {
    val monkeys: MutableList<Monkey> = ArrayList()

    var lineIndex = 0
    while (lineIndex < input.size) {
        lineIndex++
        val items = LinkedList(input[lineIndex].split(": ")[1].replace(",", "").split(" ").map { it.toLong() })
        lineIndex++
        val operationParts = input[lineIndex].split(" ")
        val operation = if (operationParts[operationParts.size-2] == "*" && operationParts.last() == "old") {
            Operation.POWER
        } else if (operationParts[operationParts.size-2] == "*") {
            Operation.MULTIPLICATION
        } else {
            Operation.ADDITION
        }
        val operationQuantity = if (operation == Operation.POWER) { 2 } else { operationParts.last().toInt() }
        lineIndex++
        val testDivisibleBy = input[lineIndex].split(" ").last().toInt()
        lineIndex++
        val trueMonkey = input[lineIndex].split(" ").last().toInt()
        lineIndex++
        val falseMonkey = input[lineIndex].split(" ").last().toInt()
        monkeys.add(Monkey(items, operation, operationQuantity, testDivisibleBy, trueMonkey, falseMonkey))
        lineIndex++
        lineIndex++
    }

    return monkeys
}

private data class Monkey(var items: Queue<Long>, val worryOperation: Operation, val worryQuantity: Int, val testDivisibleBy: Int,
    val testTrueMonkey: Int, val testFalseMonkey: Int, var inspections: Int = 0) {
    fun updateWorryPuzzle01() {
        items = LinkedList(items.map { when (worryOperation) {
                Operation.ADDITION -> (it + worryQuantity) / 3
                Operation.MULTIPLICATION -> (it * worryQuantity) / 3
                Operation.POWER -> (it * it) / 3
            }
        })
        inspections += items.size
    }

    fun updateWorryPuzzle02(lcm: Long) {
        items = LinkedList(items.map { when (worryOperation) {
            Operation.ADDITION -> (it + worryQuantity) % lcm
            Operation.MULTIPLICATION -> (it * worryQuantity) % lcm
            Operation.POWER -> (it * it) % lcm
        }
        })
        inspections += items.size
    }
}

private enum class Operation {
    ADDITION,
    MULTIPLICATION,
    POWER
}

fun day11Puzzle02(): String {
    val monkeys = parseMonkeys(readDayInput(11))
    //val lcm = findLcm(monkeys.map { it.testDivisibleBy })
    val lcm = monkeys.map { it.testDivisibleBy.toLong() }.fold(1L) { acc, n -> acc * n }
    for (i in 0 until 10000) {
        for (monkey in monkeys) {
            monkey.updateWorryPuzzle02(lcm)
            while (monkey.items.isNotEmpty()) {
                val item = monkey.items.remove()
                if (item % monkey.testDivisibleBy == 0L) {
                    monkeys[monkey.testTrueMonkey].items.add(item)
                } else {
                    monkeys[monkey.testFalseMonkey].items.add(item)
                }
            }
        }
    }

    return determineMonkeyBusiness(monkeys).toString()
}

private fun findLcm(numbers: List<Int>): Int {
    var runningLcm = 1
    for (number in numbers) {
        runningLcm = findLcm(runningLcm, number)
    }

    return runningLcm
}

private fun findLcm(a: Int, b: Int): Int {
    return (a / findGcd(a, b)) * b
}

private fun findGcd(a: Int, b: Int): Int {
    if (a == 0) {
        return b
    } else if (b == 0) {
        return a
    }

    return findGcd(b, a % b)
}
