package io.joshatron.aoc2022.days.day15

import io.joshatron.aoc2022.readDayInput
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun day15Puzzle01(): String {
    val sensorsAndBeacons = parseSensorsAndBeacons(readDayInput(15))

    var total = 0
    for (x in minX(sensorsAndBeacons)..maxX(sensorsAndBeacons)) {
        for (sensorAndBeacon in sensorsAndBeacons) {
            if (manhattanDistance(sensorAndBeacon.sensor, Coordinate(x, 2000000)) <= sensorAndBeacon.manhattanToBeacon) {
                total++
                break
            }
        }
    }

    val foundX: MutableSet<Int> = HashSet()
    for (sensorAndBeacon in sensorsAndBeacons) {
        if (sensorAndBeacon.beacon.y == 2000000 && !foundX.contains(sensorAndBeacon.beacon.x)) {
            foundX.add(sensorAndBeacon.beacon.x)
            total--
        }
    }

    return total.toString()
}

private fun minX(sensorsAndBeacons: List<SensorAndBeacon>): Int {
    return sensorsAndBeacons.fold(Int.MAX_VALUE) { acc, sb -> min(acc, sb.sensor.x - sb.manhattanToBeacon) }
}

private fun maxX(sensorsAndBeacons: List<SensorAndBeacon>): Int {
    return sensorsAndBeacons.fold(Int.MIN_VALUE) { acc, sb -> max(acc, sb.sensor.x + sb.manhattanToBeacon) }
}

private fun parseSensorsAndBeacons(input: List<String>): List<SensorAndBeacon> {
    return input.map(fun (line): SensorAndBeacon {
        val mainParts = line.split(": ")
        val sensorParts = mainParts[0].split(" ")
        val sensor = Coordinate(stripOutNonNumber(sensorParts[2]), stripOutNonNumber(sensorParts[3]))
        val beaconParts = mainParts[1].split(" ")
        val beacon = Coordinate(stripOutNonNumber(beaconParts[4]), stripOutNonNumber(beaconParts[5]))
        return SensorAndBeacon(sensor, beacon)
    })
}

private fun stripOutNonNumber(str: String): Int {
    return if (str.endsWith(",")) {
        str.substring(2, str.length-1).toInt()
    } else {
        str.substring(2).toInt()
    }
}

private data class SensorAndBeacon(val sensor: Coordinate, val beacon: Coordinate) {
    val manhattanToBeacon = manhattanDistance(sensor, beacon)
}

private fun manhattanDistance(c1: Coordinate, c2: Coordinate): Int = abs(c1.x-c2.x) + abs(c1.y-c2.y)

private data class Coordinate(val x: Int, val y: Int)

fun day15Puzzle02(): String {
    val sensorsAndBeacons = parseSensorsAndBeacons(readDayInput(15))

    for (sensorAndBeacon in sensorsAndBeacons) {
        for (y in max(sensorAndBeacon.sensor.y-sensorAndBeacon.manhattanToBeacon,0)..min(sensorAndBeacon.sensor.y+sensorAndBeacon.manhattanToBeacon,4000000)) {
            val minX = max(sensorAndBeacon.sensor.x-(sensorAndBeacon.manhattanToBeacon - abs(sensorAndBeacon.sensor.y-y)) - 1,0)
            val maxX = min(sensorAndBeacon.sensor.x+(sensorAndBeacon.manhattanToBeacon - abs(sensorAndBeacon.sensor.y-y)) + 1,4000000)

            var found = false
            for (s in sensorsAndBeacons) {
                if (manhattanDistance(s.sensor, Coordinate(minX, y)) <= s.manhattanToBeacon) {
                    found = true
                    break
                }
            }
            if (!found) {
                return ((minX * 4000000L) + y).toString()
            }
            found = false
            for (s in sensorsAndBeacons) {
                if (manhattanDistance(s.sensor, Coordinate(maxX, y)) <= s.manhattanToBeacon) {
                    found = true
                    break
                }
            }
            if (!found) {
                return ((maxX * 4000000L) + y).toString()
            }
        }
    }

    return "0"
}