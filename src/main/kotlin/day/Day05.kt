package io.joshatron.aoc2022.day

import io.joshatron.aoc2022.readDayInput

fun day05Puzzle01(): String {
    val stacksAndProcedures = parseInputToStacksAndProcedures(readDayInput(5))
    val finalStacks = applyProceduresToStacksPuzzle01(stacksAndProcedures)
    return getTopsOfStacks(finalStacks)
}

private fun parseInputToStacksAndProcedures(input: List<String>): StacksAndProcedures {
    val stacksLines = ArrayList<String>()
    val proceduresLines = ArrayList<String>()

    var onStacks = true
    for (line in input) {
        if (onStacks) {
            if (line.isBlank()) {
                stacksLines.removeAt(stacksLines.size - 1)
                onStacks = false
            } else {
                stacksLines.add(line)
            }
        } else {
            proceduresLines.add(line)
        }
    }

    return StacksAndProcedures(parseStacks(stacksLines), parseProcedures(proceduresLines))
}

private fun parseStacks(stackLines: List<String>): List<MutableList<Char>> {
    val numberOfStacks = stackLines[stackLines.size-1].split(" ").size
    val stacks = List(numberOfStacks) { ArrayList<Char>() }

    for (line in stackLines.reversed()) {
        for (spot in 1..(numberOfStacks*4-3) step 4) {
            if (line.length > spot && line[spot] != ' ') {
                stacks[(spot-1)/4].add(line[spot])
            }
        }
    }

    return stacks
}

private fun parseProcedures(proceduresLines: List<String>): List<Procedure> {
    return proceduresLines.map(fun (line: String): Procedure {
        val parts = line.split(" ")
        return Procedure(parts[1].toInt(), parts[3].toInt()-1, parts[5].toInt()-1)
    })
}

private class StacksAndProcedures(val stacks: List<MutableList<Char>>, val procedures: List<Procedure>)

private class Procedure(val quantity: Int, val start: Int, val end: Int)

private fun applyProceduresToStacksPuzzle01(stacksAndProcedures: StacksAndProcedures): List<MutableList<Char>> {
    for (procedure in stacksAndProcedures.procedures) {
        for (i in 1..procedure.quantity) {
            stacksAndProcedures.stacks[procedure.end].add(stacksAndProcedures.stacks[procedure.start].removeLast())
        }
    }

    return stacksAndProcedures.stacks
}

private fun getTopsOfStacks(stacks: List<MutableList<Char>>): String {
    return stacks.map { it.last() }
        .fold("") { acc, cur -> "$acc$cur" }
}

fun day05Puzzle02(): String {
    val stacksAndProcedures = parseInputToStacksAndProcedures(readDayInput(5))
    val finalStacks = applyProceduresToStacksPuzzle02(stacksAndProcedures)
    return getTopsOfStacks(finalStacks)
}
private fun applyProceduresToStacksPuzzle02(stacksAndProcedures: StacksAndProcedures): List<MutableList<Char>> {
    for (procedure in stacksAndProcedures.procedures) {
        var toMove = ArrayList<Char>()
        stacksAndProcedures.stacks[procedure.start]
        for (i in 1..procedure.quantity) {
            toMove.add(stacksAndProcedures.stacks[procedure.start].removeLast())
        }

        for (c in toMove.reversed()) {
            stacksAndProcedures.stacks[procedure.end].add(c)
        }
    }

    return stacksAndProcedures.stacks
}
