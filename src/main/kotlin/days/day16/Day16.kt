package io.joshatron.aoc2022.days.day16

import io.joshatron.aoc2022.readDayInput
import java.util.LinkedList
import java.util.Queue
import kotlin.math.min

fun day16Puzzle01(): String {
    val rooms = parseRooms(readDayInput(16))
    return findOptimalFlowRate(rooms).toString()
}

private fun findOptimalFlowRate(rooms: Rooms): Int {
    return findOptimalFlowRate(rooms, "AA", HashSet(), 0, 30)
}

private data class OptimalCacheEntry(val current: String, val started: Set<String>, val currentTime: Int)

private val optimalCache: MutableMap<OptimalCacheEntry, Int> = HashMap()

private fun findOptimalFlowRate(rooms: Rooms, current: String, started: Set<String>, total: Int, currentTime: Int): Int {
    if (started.size == rooms.roomsWithFlowRate.size) {
        return total
    }
    val currentOptimal = ((30 - currentTime) / 30.0) * rooms.optimalFlowRate
    if (total * 4 < currentOptimal) {
        return total
    }
    val entry = OptimalCacheEntry(current, started, currentTime)
    if (optimalCache.contains(entry)) {
        return optimalCache[entry]!!
    }

    var max = total
    for (next in rooms.roomsWithFlowRate.filter { !started.contains(it) }) {
        val dist = distanceBetweenRooms(rooms, current, next)
        val nextTime = currentTime - dist - 1
        if (nextTime >= 0) {
            val nextStarted = started.toMutableSet()
            nextStarted.add(next)
            val best = findOptimalFlowRate(
                rooms,
                next,
                nextStarted,
                total + (rooms.rooms[next]!!.flowRate * nextTime),
                nextTime
            )
            if (best > max) {
                max = best
            }
        }
    }

    optimalCache[entry] = max
    return max
}

private var distanceCache: MutableMap<String, Int> = HashMap()
private fun distanceBetweenRooms(rooms: Rooms, start: String, end: String): Int {
    if (distanceCache[start + end] != null) {
        return distanceCache[start + end]!!
    }
    val visited: MutableSet<String> = HashSet()
    val openList: Queue<RoomDistance> = LinkedList()
    openList.add(RoomDistance(start, 0))
    while (openList.isNotEmpty()) {
        val next = openList.remove()
        if (next.room == end) {
            distanceCache[start + end] = next.distance
            distanceCache[end + start] = next.distance
            return next.distance
        } else {
            visited.add(next.room)
            for (neighbor in rooms.rooms[next.room]!!.neighbors) {
                if (!visited.contains(neighbor)) {
                    openList.add(RoomDistance(neighbor, next.distance + 1))
                }
            }
        }
    }

    return 0
}

private data class RoomDistance(val room: String, val distance: Int)

private fun parseRooms(input: List<String>): Rooms {
    val rooms: MutableMap<String,Room> = HashMap()
    for (line in input) {
        val mainParts = line.split("; ")
        val name = mainParts[0].split(" ")[1]
        val flowRate = mainParts[0].split("=")[1].toInt()
        val neighborParts = mainParts[1].replace(",", "").split(" ")
        val neighbors = neighborParts.subList(4, neighborParts.size)
        rooms[name] = Room(name, neighbors, flowRate)
    }

    return Rooms(rooms)
}

private class Rooms(val rooms: Map<String,Room>) {
    val roomsWithFlowRate = rooms.values.filter { it.flowRate > 0 }.map { it.name }.toSet()
    val optimalFlowRate = rooms.values.fold(0) { acc, room -> acc + (room.flowRate * 30) }
}

private data class Room(val name: String, val neighbors: List<String>, val flowRate: Int)

fun day16Puzzle02(): String {
    val rooms = parseRooms(readDayInput(16))
    return findOptimalFlowRatePuzzle02(rooms).toString()
}

private fun findOptimalFlowRatePuzzle02(rooms: Rooms): Int {
    return findOptimalFlowRatePuzzle02(rooms, "AA", "AA", HashSet(), 0, 26, 26)
}

private data class OptimalCacheEntryPuzzle02(val currentPerson: String, val currentElephant: String, val started: Set<String>, val currentTimePerson: Int, val currentTimeElephant: Int)

private val optimalCachePuzzle02: MutableMap<OptimalCacheEntryPuzzle02, Int> = HashMap()

private fun findOptimalFlowRatePuzzle02(rooms: Rooms, currentPerson: String, currentElephant: String, started: Set<String>, total: Int, currentTimePerson: Int, currentTimeElephant: Int): Int {
    if (started.size == rooms.roomsWithFlowRate.size) {
        return total
    }
    val currentOptimal = ((26 - min(currentTimeElephant, currentTimePerson)) / 26.0) * rooms.optimalFlowRate
    if (total * 3 < currentOptimal) {
        return total
    }
    val entry = OptimalCacheEntryPuzzle02(currentPerson, currentElephant, started, currentTimePerson, currentTimeElephant)
    if (optimalCachePuzzle02.contains(entry)) {
        return optimalCachePuzzle02[entry]!!
    }

    var max = total
    for (next in rooms.roomsWithFlowRate.filter { !started.contains(it) }) {
        if (currentTimePerson > currentTimeElephant) {
            val distPerson = distanceBetweenRooms(rooms, currentPerson, next)
            val nextTimePerson = currentTimePerson - distPerson - 1
            if (nextTimePerson >= 0) {
                val nextStarted = started.toMutableSet()
                nextStarted.add(next)
                val best = findOptimalFlowRatePuzzle02(
                    rooms,
                    next,
                    currentElephant,
                    nextStarted,
                    total + (rooms.rooms[next]!!.flowRate * nextTimePerson),
                    nextTimePerson,
                    currentTimeElephant
                )
                if (best > max) {
                    max = best
                }
            }
        } else {
            val distElephant = distanceBetweenRooms(rooms, currentElephant, next)
            val nextTimeElephant = currentTimeElephant - distElephant - 1
            if (nextTimeElephant >= 0) {
                val nextStarted = started.toMutableSet()
                nextStarted.add(next)
                val best = findOptimalFlowRatePuzzle02(
                    rooms,
                    currentPerson,
                    next,
                    nextStarted,
                    total + (rooms.rooms[next]!!.flowRate * nextTimeElephant),
                    currentTimePerson,
                    nextTimeElephant
                )
                if (best > max) {
                    max = best
                }
            }
        }
    }

    optimalCachePuzzle02[entry] = max
    return max
}