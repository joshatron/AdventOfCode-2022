package io.joshatron.aoc2022.day

import io.joshatron.aoc2022.readDayInput

fun day10Puzzle01(): String {
    val instructions = parseDay10Instructions(readDayInput(10))

    var x = 1
    var cycle = 0
    var sum = 0
    for (instruction in instructions) {
        if (instruction.type == InstructionType.NOOP) {
            cycle++
            if (isRecordCycle(cycle)) {
                sum += x * cycle
            }
        } else {
            cycle++
            if (isRecordCycle(cycle)) {
                sum += x * cycle
            }
            cycle++
            if (isRecordCycle(cycle)) {
                sum += x * cycle
            }
            x += instruction.value
        }
    }

    return sum.toString();
}

private fun isRecordCycle(cycle: Int): Boolean {
    return cycle == 20 || cycle == 60 || cycle == 100 || cycle == 140 || cycle == 180 || cycle == 220
}

private fun parseDay10Instructions(input: List<String>): List<Day10Instruction> {
    return input.map(fun (line): Day10Instruction {
        val parts = line.split(" ")
        return if (parts.size == 1) {
            Day10Instruction(InstructionType.NOOP, 0)
        } else {
            Day10Instruction(InstructionType.ADDX, parts[1].toInt())
        }
    })
}

private data class Day10Instruction(val type: InstructionType, val value: Int)

private enum class InstructionType {
    NOOP,
    ADDX
}

fun day10Puzzle02(): String {
    val instructions = parseDay10Instructions(readDayInput(10))

    var x = 1
    var cycle = -1
    var crt = "\n"
    for (instruction in instructions) {
        if (instruction.type == InstructionType.NOOP) {
            cycle++
            crt += spriteChar(cycle, x)
        } else {
            cycle++
            crt += spriteChar(cycle, x)
            cycle++
            crt += spriteChar(cycle, x)
            x += instruction.value
        }
    }

    return crt
}

private fun spriteChar(cycle: Int, x: Int): String {
    var str = if (cycle % 40 == x || cycle % 40 == x - 1 || cycle % 40 == x + 1) {
        "██"
    } else {
        "  "
    }
    if ((cycle + 1) % 40 == 0) {
        str += "\n"
    }

    return str
}
