package io.joshatron.aoc2022.days.day12

import io.joshatron.aoc2022.readDayInput
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.streams.toList

fun day12Puzzle01(): String {
    val elevationMap = parseElevationMap(readDayInput(12))
    return navigateBfsUp(elevationMap).toString()
}


private fun navigateBfsUp(elevationMap: ElevationMap): Int {
    val startCoordinate = elevationMap.findStart()
    val endCoordinate = elevationMap.findEnd()

    val openList: Queue<BFSCoordinate> = LinkedList()
    val closedList: MutableSet<Coordinate> = HashSet()
    openList.add(BFSCoordinate(startCoordinate, 0))

    while (openList.isNotEmpty()) {
        val nextCoord = openList.remove()
        if (nextCoord.coordinate == endCoordinate) {
            return nextCoord.steps
        }
        closedList.add(nextCoord.coordinate)
        for (possible in elevationMap.possibleMovesUp(nextCoord.coordinate)) {
            if (!closedList.contains(possible) && !openList.any { it.coordinate == possible }) {
                openList.add(BFSCoordinate(possible, nextCoord.steps + 1))
            }
        }
    }

    return 0
}

private data class BFSCoordinate(val coordinate: Coordinate, val steps: Int)

private fun parseElevationMap(input: List<String>): ElevationMap {
    return ElevationMap(input.map { line -> line.chars()
            .map { c -> if (c == 83) { 0 } else if (c == 69) { 27 } else { c - 96 } }
            .toList() })
}

private class ElevationMap(val elevations: List<List<Int>>) {
    val width = elevations[0].size
    val height = elevations.size

    fun findStart(): Coordinate {
        return findValue(0)
    }

    fun findEnd(): Coordinate {
        return findValue(27)
    }

    private fun findValue(value: Int): Coordinate {
        for (y in elevations.indices) {
            for (x in elevations[0].indices) {
                if (elevations[y][x] == value) {
                    return Coordinate(x, y)
                }
            }
        }

        return Coordinate(0,0)
    }

    fun elevationAt(coordinate: Coordinate): Int {
        return when(val elevation = elevations[coordinate.y][coordinate.x]) {
            0 -> 1
            27 -> 26
            else -> elevation
        }
    }

    fun possibleMovesUp(coordinate: Coordinate): List<Coordinate> {
        val currentElevation = elevationAt(coordinate)

        val possible: MutableList<Coordinate> = ArrayList()
        if (coordinate.x < width-1 && currentElevation + 1 >= elevationAt(coordinate.copy(x = coordinate.x + 1))) {
            possible.add(coordinate.copy(x = coordinate.x + 1))
        }
        if (coordinate.x > 0 && currentElevation + 1 >= elevationAt(coordinate.copy(x = coordinate.x - 1))) {
            possible.add(coordinate.copy(x = coordinate.x - 1))
        }
        if (coordinate.y < height-1 && currentElevation + 1 >= elevationAt(coordinate.copy(y = coordinate.y + 1))) {
            possible.add(coordinate.copy(y = coordinate.y + 1))
        }
        if (coordinate.y > 0 && currentElevation + 1 >= elevationAt(coordinate.copy(y = coordinate.y - 1))) {
            possible.add(coordinate.copy(y = coordinate.y - 1))
        }

        return possible
    }

    fun possibleMovesDown(coordinate: Coordinate): List<Coordinate> {
        val currentElevation = elevationAt(coordinate)

        val possible: MutableList<Coordinate> = ArrayList()
        if (coordinate.x < width-1 && currentElevation - 1 <= elevationAt(coordinate.copy(x = coordinate.x + 1))) {
            possible.add(coordinate.copy(x = coordinate.x + 1))
        }
        if (coordinate.x > 0 && currentElevation - 1 <= elevationAt(coordinate.copy(x = coordinate.x - 1))) {
            possible.add(coordinate.copy(x = coordinate.x - 1))
        }
        if (coordinate.y < height-1 && currentElevation - 1 <= elevationAt(coordinate.copy(y = coordinate.y + 1))) {
            possible.add(coordinate.copy(y = coordinate.y + 1))
        }
        if (coordinate.y > 0 && currentElevation - 1 <= elevationAt(coordinate.copy(y = coordinate.y - 1))) {
            possible.add(coordinate.copy(y = coordinate.y - 1))
        }

        return possible
    }
}

private data class Coordinate(val x: Int, val y: Int)

fun day12Puzzle02(): String {
    val elevationMap = parseElevationMap(readDayInput(12))
    return navigateBfsDown(elevationMap).toString()
}

private fun navigateBfsDown(elevationMap: ElevationMap): Int {
    val startCoordinate = elevationMap.findEnd()

    val openList: Queue<BFSCoordinate> = LinkedList()
    val closedList: MutableSet<Coordinate> = HashSet()
    openList.add(BFSCoordinate(startCoordinate, 0))

    while (openList.isNotEmpty()) {
        val nextCoord = openList.remove()
        if (elevationMap.elevationAt(nextCoord.coordinate) == 1) {
            return nextCoord.steps
        }
        closedList.add(nextCoord.coordinate)
        for (possible in elevationMap.possibleMovesDown(nextCoord.coordinate)) {
            if (!closedList.contains(possible) && !openList.any { it.coordinate == possible }) {
                openList.add(BFSCoordinate(possible, nextCoord.steps + 1))
            }
        }
    }

    return 0
}