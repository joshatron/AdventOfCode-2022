package io.joshatron.aoc2022.days.day21

import io.joshatron.aoc2022.readDayInput

fun day21Puzzle01(): String {
    val monkeys = parseMonkeys(readDayInput(21))
    return determineMonkeyValue(monkeys, "root").toString()
}

private fun determineMonkeyValue(monkeys: Map<String, Monkey>, monkey: String): Long {
    if (monkeys[monkey]!!.number != null) {
        return monkeys[monkey]!!.number!!
    }

    val depends1Value = determineMonkeyValue(monkeys, monkeys[monkey]!!.depends1!!)
    monkeys[monkeys[monkey]!!.depends1]!!.number = depends1Value
    monkeys[monkeys[monkey]!!.depends1]!!.depends1 = null
    monkeys[monkeys[monkey]!!.depends1]!!.operation = null
    monkeys[monkeys[monkey]!!.depends1]!!.depends2 = null

    val depends2Value = determineMonkeyValue(monkeys, monkeys[monkey]!!.depends2!!)
    monkeys[monkeys[monkey]!!.depends2]!!.number = depends2Value
    monkeys[monkeys[monkey]!!.depends2]!!.depends1 = null
    monkeys[monkeys[monkey]!!.depends2]!!.operation = null
    monkeys[monkeys[monkey]!!.depends2]!!.depends2 = null

    return applyOperation(depends1Value, monkeys[monkey]!!.operation!!, depends2Value);
}

private fun applyOperation(number1: Long, operation: Operation, number2: Long): Long {
    return when (operation) {
        Operation.ADD -> number1 + number2
        Operation.SUBTRACT -> number1 - number2
        Operation.MULTIPLY -> number1 * number2
        Operation.DIVIDE -> number1 / number2
        Operation.EQUALS -> if (number1 == number2) 0 else 1
    }
}

private fun parseMonkeys(input: List<String>): Map<String, Monkey> {
    return input.map(fun(line): Monkey {
        val mainParts = line.split(": ")
        return if (mainParts[1].contains(" ")) {
            val operationParts = mainParts[1].split(" ")
            Monkey(mainParts[0], null, operationParts[0], operationStrToOperation(operationParts[1]), operationParts[2])
        } else {
            Monkey(mainParts[0], mainParts[1].toLong(), null, null, null)
        }

    }).associateBy {
        it.name
    }
}

private fun operationStrToOperation(str: String): Operation {
    return when (str) {
        "+" -> Operation.ADD
        "-" -> Operation.SUBTRACT
        "*" -> Operation.MULTIPLY
        "/" -> Operation.DIVIDE
        else -> Operation.ADD
    }
}

private class Monkey(val name: String, var number: Long?, var depends1: String?, var operation: Operation?, var depends2: String?)

private enum class Operation {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    EQUALS
}

fun day21Puzzle02(): String {
    val monkeys = parseMonkeys(readDayInput(21))
    val monkeyEquation = generateMonkeyEquation(monkeys, "root")
    return solveForHumn(monkeyEquation).toString()
}

private fun solveForHumn(monkeyEquation: MonkeyEquation): Long {
    while (true) {
        if (monkeyEquation.side1!!.variable && monkeyEquation.side2!!.number != null) {
            return monkeyEquation.side2!!.number!!
        }
        if (monkeyEquation.side2!!.variable && monkeyEquation.side1!!.number != null) {
            return monkeyEquation.side1!!.number!!
        }

        if (monkeyEquation.side1!!.side1!!.number != null) {
            if (monkeyEquation.side1!!.operation == Operation.SUBTRACT || monkeyEquation.side1!!.operation == Operation.DIVIDE) {
                monkeyEquation.side2!!.number = applyOperation(
                    monkeyEquation.side1!!.side1!!.number!!,
                    monkeyEquation.side1!!.operation!!,
                    monkeyEquation.side2!!.number!!
                )
            } else {
                monkeyEquation.side2!!.number = applyOppositeOperation(
                    monkeyEquation.side2!!.number!!,
                    monkeyEquation.side1!!.operation!!,
                    monkeyEquation.side1!!.side1!!.number!!
                )
            }
            monkeyEquation.side1 = monkeyEquation.side1!!.side2
        } else if (monkeyEquation.side1!!.side2!!.number != null) {
            monkeyEquation.side2!!.number = applyOppositeOperation(
                monkeyEquation.side2!!.number!!,
                monkeyEquation.side1!!.operation!!,
                monkeyEquation.side1!!.side2!!.number!!)
            monkeyEquation.side1 = monkeyEquation.side1!!.side1
        }
    }
}

private fun applyOppositeOperation(number1: Long, operation: Operation, number2: Long): Long {
    return when (operation) {
        Operation.ADD -> number1 - number2
        Operation.SUBTRACT -> number1 + number2
        Operation.MULTIPLY -> number1 / number2
        Operation.DIVIDE -> number1 * number2
        Operation.EQUALS -> if (number1 == number2) 0 else 1
    }
}

private fun generateMonkeyEquation(monkeys: Map<String, Monkey>, current: String): MonkeyEquation {
    if (current == "humn") {
        return MonkeyEquation(true, null, null, null, null)
    }

    if (current == "root") {
        return MonkeyEquation(false, null, generateMonkeyEquation(monkeys, monkeys[current]!!.depends1!!), Operation.EQUALS, generateMonkeyEquation(monkeys, monkeys[current]!!.depends2!!))
    }

    if (monkeys[current]!!.number != null) {
        return MonkeyEquation(false, monkeys[current]!!.number, null, null, null)
    }

    val side1 = generateMonkeyEquation(monkeys, monkeys[current]!!.depends1!!)
    val side2 = generateMonkeyEquation(monkeys, monkeys[current]!!.depends2!!)
    if (side1.number != null && side2.number != null) {
        return MonkeyEquation(false, applyOperation(side1.number!!, monkeys[current]!!.operation!!, side2.number!!), null, null, null)
    }
    return MonkeyEquation(false, null, side1, monkeys[current]!!.operation, side2)
}

private data class MonkeyEquation(var variable: Boolean, var number: Long?, var side1: MonkeyEquation?, var operation: Operation?, var side2: MonkeyEquation?)