package io.joshatron.aoc2022.days.day23

import io.joshatron.aoc2022.readDayInput
import java.lang.Integer.max
import kotlin.math.min

fun day23Puzzle01(): String {
    val elfPositionsAfter10Rounds = runSimulation(parseElfCoordinates(readDayInput(23)), 10).elves
    val minX = elfPositionsAfter10Rounds.fold(Int.MAX_VALUE) { acc, elf -> min(acc, elf.x)}
    val maxX = elfPositionsAfter10Rounds.fold(Int.MIN_VALUE) { acc, elf -> max(acc, elf.x)}
    val minY = elfPositionsAfter10Rounds.fold(Int.MAX_VALUE) { acc, elf -> min(acc, elf.y)}
    val maxY = elfPositionsAfter10Rounds.fold(Int.MIN_VALUE) { acc, elf -> max(acc, elf.y)}

    return (((maxX - minX + 1) * (maxY - minY + 1)) - elfPositionsAfter10Rounds.size).toString()
}

private fun runSimulation(initial: Set<Coordinate>, maxRounds: Int): RoundAndElves {
    var round = 0
    val current: MutableSet<Coordinate> = HashSet(initial)
    while (true) {
        val proposals = getProposed(current, round)

        if (proposals.isEmpty()) {
            return RoundAndElves(round + 1, current)
        }

        for (proposal in proposals) {
            if (proposal.value.size == 1) {
                current.remove(proposal.value[0])
                current.add(proposal.key)
            }
        }
        round++
        if (maxRounds > 0 && round == maxRounds) {
            return RoundAndElves(round + 1, current)
        }
    }
}

private class RoundAndElves(val round: Int, val elves: Set<Coordinate>)

private fun getProposed(elves: Set<Coordinate>, round: Int): Map<Coordinate, List<Coordinate>> {
    val proposals: MutableMap<Coordinate, MutableList<Coordinate>> = HashMap()

    val toCheck: MutableList<Direction> = arrayListOf(Direction.NORTH, Direction.NORTHWEST, Direction.NORTHEAST,
        Direction.SOUTH, Direction.SOUTHWEST, Direction.SOUTHEAST,
        Direction.WEST, Direction.NORTHWEST, Direction.SOUTHWEST,
        Direction.EAST, Direction.NORTHEAST, Direction.SOUTHEAST)
    repeat(3 * (round % 4)) {
        toCheck.add(toCheck.removeFirst())
    }

    for (elf in elves) {
        val neighbors = getNeighbors(elf, elves)
        if (neighbors.isNotEmpty()) {
            for (i in 0 until 4) {
                if (!neighbors.contains(toCheck[i*3]) && !neighbors.contains(toCheck[i*3+1]) && !neighbors.contains(toCheck[i*3+2])) {
                    val proposal = move(elf, toCheck[i*3])
                    if (proposals.contains(proposal)) {
                        proposals[proposal]?.add(elf)
                    } else {
                        proposals[proposal] = arrayListOf(elf)
                    }
                    break
                }
            }
        }
    }

    return proposals
}

private fun getNeighbors(elf: Coordinate, elves: Set<Coordinate>): Set<Direction> {
    return arrayListOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST,
        Direction.NORTHWEST, Direction.NORTHEAST, Direction.SOUTHWEST, Direction.SOUTHEAST)
        .filter { elves.contains(move(elf, it)) }.toSet()
}

private fun move(initial: Coordinate, direction: Direction): Coordinate {
    return when (direction) {
        Direction.NORTH -> initial.copy(y = initial.y - 1)
        Direction.SOUTH -> initial.copy(y = initial.y + 1)
        Direction.EAST -> initial.copy(x = initial.x + 1)
        Direction.WEST -> initial.copy(x = initial.x - 1)
        Direction.NORTHWEST -> initial.copy(x = initial.x - 1, y = initial.y - 1)
        Direction.NORTHEAST -> initial.copy(x = initial.x + 1, y = initial.y - 1)
        Direction.SOUTHWEST -> initial.copy(x = initial.x - 1, y = initial.y + 1)
        Direction.SOUTHEAST -> initial.copy(x = initial.x + 1, y = initial.y + 1)
    }
}

private enum class Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    NORTHWEST,
    NORTHEAST,
    SOUTHWEST,
    SOUTHEAST
}

private fun parseElfCoordinates(input: List<String>): Set<Coordinate> {
    val elves: MutableSet<Coordinate> = HashSet()
    for (y in input.indices) {
        for (x in 0 until input[y].length) {
            if (input[y][x] == '#') {
                elves.add(Coordinate(x, y))
            }
        }
    }

    return elves
}

private data class Coordinate(val x: Int, val y: Int)

fun day23Puzzle02(): String {
    val rounds = runSimulation(parseElfCoordinates(readDayInput(23)), 0).round
    return rounds.toString()
}
