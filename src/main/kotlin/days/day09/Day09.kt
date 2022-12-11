package io.joshatron.aoc2022.days.day09

import io.joshatron.aoc2022.readDayInput

fun day09Puzzle01(): String {
    return getTailCoordinatesOfSimulation(parseInstructions(readDayInput(9)), 2).size.toString()
}

private fun getTailCoordinatesOfSimulation(instructions: List<Instruction>, numberOfSegments: Int): Set<Coordinate> {
    val tailCoordinates: MutableSet<Coordinate> = HashSet()
    val segments: MutableList<Coordinate> = ArrayList()
    repeat(numberOfSegments) {
        segments.add(Coordinate(0, 0))
    }
    tailCoordinates.add(segments.last())

    for (instruction in instructions) {
        repeat(instruction.quantity) {
            segments[0] = moveInDirection(segments[0], instruction.direction)
            for (i in 1 until segments.size) {
                segments[i] = moveInDirection(segments[i], getTailDirectionToMove(segments[i-1], segments[i]))
            }
            tailCoordinates.add(segments.last())
        }
    }

    return tailCoordinates
}

private fun getTailDirectionToMove(head: Coordinate, tail: Coordinate): Direction {
    return when {
        head.x == tail.x + 2 && head.y == tail.y -> Direction.RIGHT
        head.x == tail.x - 2 && head.y == tail.y -> Direction.LEFT
        head.x == tail.x && head.y == tail.y + 2 -> Direction.UP
        head.x == tail.x && head.y == tail.y - 2 -> Direction.DOWN
        head.x == tail.x + 1 && head.y == tail.y + 2 -> Direction.UP_RIGHT
        head.x == tail.x + 2 && head.y == tail.y + 1 -> Direction.UP_RIGHT
        head.x == tail.x + 2 && head.y == tail.y + 2 -> Direction.UP_RIGHT
        head.x == tail.x - 1 && head.y == tail.y + 2 -> Direction.UP_LEFT
        head.x == tail.x - 2 && head.y == tail.y + 1 -> Direction.UP_LEFT
        head.x == tail.x - 2 && head.y == tail.y + 2 -> Direction.UP_LEFT
        head.x == tail.x + 1 && head.y == tail.y - 2 -> Direction.DOWN_RIGHT
        head.x == tail.x + 2 && head.y == tail.y - 1 -> Direction.DOWN_RIGHT
        head.x == tail.x + 2 && head.y == tail.y - 2 -> Direction.DOWN_RIGHT
        head.x == tail.x - 1 && head.y == tail.y - 2 -> Direction.DOWN_LEFT
        head.x == tail.x - 2 && head.y == tail.y - 1 -> Direction.DOWN_LEFT
        head.x == tail.x - 2 && head.y == tail.y - 2 -> Direction.DOWN_LEFT
        else -> Direction.NONE
    }
}

private fun moveInDirection(coordinate: Coordinate, direction: Direction): Coordinate {
    return when (direction) {
        Direction.UP -> Coordinate(coordinate.x, coordinate.y + 1)
        Direction.DOWN -> Coordinate(coordinate.x, coordinate.y - 1)
        Direction.LEFT -> Coordinate(coordinate.x - 1, coordinate.y)
        Direction.RIGHT -> Coordinate(coordinate.x + 1, coordinate.y)
        Direction.UP_LEFT -> Coordinate(coordinate.x - 1, coordinate.y + 1)
        Direction.UP_RIGHT -> Coordinate(coordinate.x + 1, coordinate.y + 1)
        Direction.DOWN_LEFT -> Coordinate(coordinate.x - 1, coordinate.y - 1)
        Direction.DOWN_RIGHT -> Coordinate(coordinate.x + 1, coordinate.y - 1)
        Direction.NONE -> Coordinate(coordinate.x, coordinate.y)
    }
}

private fun parseInstructions(input: List<String>): List<Instruction> {
    return input.map(fun (line: String): Instruction {
        val parts = line.split(" ")
        return Instruction(stringToDirection(parts[0]), parts[1].toInt())
    })
}

private class Instruction(val direction: Direction, val quantity: Int)

private enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    UP_LEFT,
    UP_RIGHT,
    DOWN_LEFT,
    DOWN_RIGHT,
    NONE
}

private fun stringToDirection(str: String): Direction {
    return when (str) {
        "U" -> Direction.UP
        "D" -> Direction.DOWN
        "L" -> Direction.LEFT
        "R" -> Direction.RIGHT
        else -> Direction.NONE
    }
}

private data class Coordinate(val x: Int, val y: Int)

fun day09Puzzle02(): String {
    return getTailCoordinatesOfSimulation(parseInstructions(readDayInput(9)), 10).size.toString()
}
