package io.joshatron.aoc2022.days.day12

import io.joshatron.aoc2022.readDayInput
import java.util.PriorityQueue
import kotlin.math.abs
import kotlin.streams.toList

fun day12Puzzle01(): String {
    val elevationMap = parseElevationMap(readDayInput(12))
    val navigation = navigateFromStartToEnd(elevationMap)
    return navigation.size.toString()
}

private fun navigateFromStartToEnd(elevationMap: ElevationMap): List<Coordinate> {
    val startCoordinate = elevationMap.findStart()
    val endCoordinate = elevationMap.findEnd()

    val aStarComparer: Comparator<AStarCoordinate> = compareBy { it.f }

    val openList: PriorityQueue<AStarCoordinate> = PriorityQueue(aStarComparer)
    val closedList: PriorityQueue<AStarCoordinate> = PriorityQueue(aStarComparer)
    openList.add(AStarCoordinate(startCoordinate, 0, 0, null))

    while (openList.isNotEmpty()) {
        val q = openList.remove()
        //println("(${q.coordinate.x}, ${q.coordinate.y}) f: ${q.f}, open: ${openList.size}, closed: ${closedList.size}")

        val children = elevationMap.possibleMovesUp(q.coordinate).map { AStarCoordinate(it, q.g + 1, determineH(it, elevationMap.elevationAt(it), endCoordinate), q) }
        for (child in children) {
            if (child.coordinate == endCoordinate) {
                return unpackPath(child)
            }
            var addToOpenList = true
            for (c in openList) {
                if (child.coordinate == c.coordinate && c.f <= child.f) {
                    addToOpenList = false
                    break
                }
                if (c.f > child.f) {
                    break
                }
            }
            for (c in closedList) {
                if (child.coordinate == c.coordinate && c.f <= child.f) {
                    addToOpenList = false
                    break
                }
                if (c.f > child.f) {
                    break
                }
            }
            if (addToOpenList) {
                openList.add(child)
            }
        }
        closedList.add(q)
    }

    return ArrayList()
}

private fun unpackPath(finalCoord: AStarCoordinate): List<Coordinate> {
    val path: MutableList<Coordinate> = ArrayList()

    var currentCoord = finalCoord
    while(currentCoord.parent != null) {
        path.add(currentCoord.coordinate)
        currentCoord = currentCoord.parent!!
    }

    return path.reversed()
}

private fun determineH(coord: Coordinate, elevation: Int, endCoord: Coordinate): Int {
    //return (27 - elevation) + abs(coord.x - endCoord.x) + abs(coord.y - endCoord.y)
    return abs(coord.x - endCoord.x) + abs(coord.y - endCoord.y)
}

private data class AStarCoordinate(val coordinate: Coordinate, val g: Int, val h: Int, val parent: AStarCoordinate?) {
    val f = g + h
}

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
    val navigation = navigateFromEndToA(elevationMap)
    return navigation.size.toString()
}

private fun navigateFromEndToA(elevationMap: ElevationMap): List<Coordinate> {
    val startCoordinate = elevationMap.findEnd()

    val aStarComparer: Comparator<AStarCoordinate> = compareBy { it.f }

    val openList: PriorityQueue<AStarCoordinate> = PriorityQueue(aStarComparer)
    val closedList: PriorityQueue<AStarCoordinate> = PriorityQueue(aStarComparer)
    openList.add(AStarCoordinate(startCoordinate, 0, 0, null))

    while (openList.isNotEmpty()) {
        val q = openList.remove()

        val children = elevationMap.possibleMovesDown(q.coordinate).map { AStarCoordinate(it, q.g + 1, 0, q) }
        for (child in children) {
            if (elevationMap.elevationAt(child.coordinate) == 1) {
                return unpackPath(child)
            }
            var addToOpenList = true
            for (c in openList) {
                if (child.coordinate == c.coordinate && c.f <= child.f) {
                    addToOpenList = false
                    break
                }
                if (c.f > child.f) {
                    break
                }
            }
            for (c in closedList) {
                if (child.coordinate == c.coordinate && c.f <= child.f) {
                    addToOpenList = false
                    break
                }
                if (c.f > child.f) {
                    break
                }
            }
            if (addToOpenList) {
                openList.add(child)
            }
        }
        closedList.add(q)
    }

    return ArrayList()
}