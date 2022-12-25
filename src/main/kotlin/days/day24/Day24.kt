package io.joshatron.aoc2022.days.day24

import io.joshatron.aoc2022.readDayInput

fun day24Puzzle01(): String {
    val blizzardMap = parseBlizzardMap(readDayInput(24))
    return findRoute(blizzardMap, blizzardMap.start, blizzardMap.end).toString()
}

private fun findRoute(blizzardMap: BlizzardMap, start: Coordinate, end: Coordinate): Int {
    var step = 0
    var states: MutableSet<Coordinate> = HashSet()
    states.add(start)
    while (true) {
        step++
        blizzardMap.step()

        val newStates: MutableSet<Coordinate> = HashSet()
        for (state in states) {
            if (!blizzardMap.blizzards.contains(state)) {
                newStates.add(state)
            }
            val upCoordinate = state.copy(y = state.y - 1)
            if (blizzardMap.inMap(upCoordinate) && !blizzardMap.blizzards.contains(upCoordinate)) {
                newStates.add(upCoordinate)
            }
            val downCoordinate = state.copy(y = state.y + 1)
            if (blizzardMap.inMap(downCoordinate) && !blizzardMap.blizzards.contains(downCoordinate)) {
                newStates.add(downCoordinate)
            }
            val leftCoordinate = state.copy(x = state.x - 1)
            if (blizzardMap.inMap(leftCoordinate) && !blizzardMap.blizzards.contains(leftCoordinate)) {
                newStates.add(leftCoordinate)
            }
            val rightCoordinate = state.copy(x = state.x + 1)
            if (blizzardMap.inMap(rightCoordinate) && !blizzardMap.blizzards.contains(rightCoordinate)) {
                newStates.add(rightCoordinate)
            }
        }
        states = newStates

        if (states.contains(end)) {
            return step
        }
    }
}

private fun parseBlizzardMap(input: List<String>): BlizzardMap {
    val minX = 1
    val minY = 1
    val maxX = input[0].length - 2
    val maxY = input.size - 2
    val start = Coordinate(input[0].indexOf("."), 0)
    val end = Coordinate(input[maxY+1].indexOf("."), maxY + 1)

    val blizzards: MutableMap<Coordinate, MutableList<Direction>> = HashMap()
    for (y in input.indices) {
        val line = input[y]
        for (x in line.indices) {
            val c = Coordinate(x, y)
            when (line[x]) {
                '^' -> if (blizzards.contains(c)) blizzards[c]!!.add(Direction.UP) else blizzards[c] = arrayListOf(Direction.UP)
                'v' -> if (blizzards.contains(c)) blizzards[c]!!.add(Direction.DOWN) else blizzards[c] = arrayListOf(Direction.DOWN)
                '<' -> if (blizzards.contains(c)) blizzards[c]!!.add(Direction.LEFT) else blizzards[c] = arrayListOf(Direction.LEFT)
                '>' -> if (blizzards.contains(c)) blizzards[c]!!.add(Direction.RIGHT) else blizzards[c] = arrayListOf(Direction.RIGHT)
            }
        }
    }

    return BlizzardMap(start, end, minX, minY, maxX, maxY, blizzards)
}

private data class BlizzardMap(val start: Coordinate, val end: Coordinate, val minX: Int, val minY: Int, val maxX: Int, val maxY: Int, var blizzards: Map<Coordinate, List<Direction>>) {
    fun step() {
        val newBlizzards: MutableMap<Coordinate, MutableList<Direction>> = HashMap()

        for (blizzard in blizzards) {
            for (direction in blizzard.value) {
                var newC = blizzard.key
                if (direction == Direction.UP) {
                    newC = newC.copy(y = newC.y - 1)
                    if (newC.y < minY) {
                        newC = Coordinate(newC.x, maxY)
                    }
                } else if (direction == Direction.DOWN) {
                    newC = newC.copy(y = newC.y + 1)
                    if (newC.y > maxY) {
                        newC = Coordinate(newC.x, minY)
                    }
                } else if (direction == Direction.LEFT) {
                    newC = newC.copy(x = newC.x - 1)
                    if (newC.x < minX) {
                        newC = Coordinate(maxX, newC.y)
                    }
                } else if (direction == Direction.RIGHT) {
                    newC = newC.copy(x = newC.x + 1)
                    if (newC.x > maxX) {
                        newC = Coordinate(minX, newC.y)
                    }
                }
                if (newBlizzards.contains(newC)) newBlizzards[newC]!!.add(direction) else newBlizzards[newC] = arrayListOf(direction)
            }
        }

        blizzards = newBlizzards
    }

    fun inMap(coordinate: Coordinate): Boolean {
        return (coordinate.x in minX..maxX && coordinate.y in minY..maxY) || coordinate == start || coordinate == end
    }
}

private data class Coordinate(val x: Int, val y: Int)

private enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

fun day24Puzzle02(): String {
    val blizzardMap = parseBlizzardMap(readDayInput(24))
    return (findRoute(blizzardMap, blizzardMap.start, blizzardMap.end) + findRoute(blizzardMap, blizzardMap.end, blizzardMap.start) + findRoute(blizzardMap, blizzardMap.start, blizzardMap.end)).toString()
    return ""
}
