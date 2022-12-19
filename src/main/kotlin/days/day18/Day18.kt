package io.joshatron.aoc2022.days.day18

import io.joshatron.aoc2022.readDayInput
import java.util.*
import kotlin.collections.HashSet

fun day18Puzzle01(): String {
    val coordinates: Set<Coordinate> = parseCoordinates(readDayInput(18))

    var total = 0
    for (coordinate in coordinates) {
        var found = false
        if (!coordinates.contains(coordinate.copy(x = coordinate.x + 1))) {
            total++
        }
        if (!coordinates.contains(coordinate.copy(x = coordinate.x - 1))) {
            total++
        }
        if (!coordinates.contains(coordinate.copy(y = coordinate.y + 1))) {
            total++
        }
        if (!coordinates.contains(coordinate.copy(y = coordinate.y - 1))) {
            total++
        }
        if (!coordinates.contains(coordinate.copy(z = coordinate.z + 1))) {
            total++
        }
        if (!coordinates.contains(coordinate.copy(z = coordinate.z - 1))) {
            total++
        }
    }

    return total.toString()
}

private fun parseCoordinates(input: List<String>): Set<Coordinate> {
    val coordinates: MutableSet<Coordinate> = HashSet()

    for (line in input) {
        val parts = line.split(",")
        coordinates.add(Coordinate(parts[0].toInt(), parts[1].toInt(), parts[2].toInt()))
    }

    return coordinates
}

private data class Coordinate(val x: Int, val y: Int, val z: Int)

fun day18Puzzle02(): String {
    val coordinates: Set<Coordinate> = parseCoordinates(readDayInput(18))
    val minXCoordinate = coordinates.reduce { acc, c -> if (acc.x < c.x) acc else c }
    val openList: Queue<Face> = LinkedList()
    val visited: MutableSet<Face> = HashSet()
    openList.add(Face(minXCoordinate, Side.LEFT))

    while (openList.isNotEmpty()) {
        val current = openList.remove()
        visited.add(current)
        val neighbors = findNeighbors(coordinates, current).filter { !visited.contains(it) && !openList.contains(it) }
        openList.addAll(neighbors)
    }

    return visited.size.toString()
}

private fun findNeighbors(coordinates: Set<Coordinate>, face: Face): List<Face> {
    val neighbors: MutableList<Face> = ArrayList()

    if (face.side == Side.LEFT) {
        if (coordinates.contains(face.coordinate.copy(y = face.coordinate.y - 1, x = face.coordinate.x - 1))) {
            neighbors.add(Face(face.coordinate.copy(y = face.coordinate.y - 1, x = face.coordinate.x - 1), Side.UP))
        } else if (coordinates.contains(face.coordinate.copy(y = face.coordinate.y - 1))) {
            neighbors.add(Face(face.coordinate.copy(y = face.coordinate.y - 1), Side.LEFT))
        } else {
            neighbors.add(Face(face.coordinate, Side.DOWN))
        }

        if (coordinates.contains(face.coordinate.copy(y = face.coordinate.y + 1, x = face.coordinate.x - 1))) {
            neighbors.add(Face(face.coordinate.copy(y = face.coordinate.y + 1, x = face.coordinate.x - 1), Side.DOWN))
        } else if (coordinates.contains(face.coordinate.copy(y = face.coordinate.y + 1))) {
            neighbors.add(Face(face.coordinate.copy(y = face.coordinate.y + 1), Side.LEFT))
        } else {
            neighbors.add(Face(face.coordinate, Side.UP))
        }

        if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z + 1, x = face.coordinate.x - 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z + 1, x = face.coordinate.x - 1), Side.BACK))
        } else if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z + 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z + 1), Side.LEFT))
        } else {
            neighbors.add(Face(face.coordinate, Side.FRONT))
        }

        if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z - 1, x = face.coordinate.x - 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z - 1, x = face.coordinate.x - 1), Side.FRONT))
        } else if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z - 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z - 1), Side.LEFT))
        } else {
            neighbors.add(Face(face.coordinate, Side.BACK))
        }
    } else if (face.side == Side.RIGHT) {
        if (coordinates.contains(face.coordinate.copy(y = face.coordinate.y - 1, x = face.coordinate.x + 1))) {
            neighbors.add(Face(face.coordinate.copy(y = face.coordinate.y - 1, x = face.coordinate.x + 1), Side.UP))
        } else if (coordinates.contains(face.coordinate.copy(y = face.coordinate.y - 1))) {
            neighbors.add(Face(face.coordinate.copy(y = face.coordinate.y - 1), Side.RIGHT))
        } else {
            neighbors.add(Face(face.coordinate, Side.DOWN))
        }

        if (coordinates.contains(face.coordinate.copy(y = face.coordinate.y + 1, x = face.coordinate.x + 1))) {
            neighbors.add(Face(face.coordinate.copy(y = face.coordinate.y + 1, x = face.coordinate.x + 1), Side.DOWN))
        } else if (coordinates.contains(face.coordinate.copy(y = face.coordinate.y + 1))) {
            neighbors.add(Face(face.coordinate.copy(y = face.coordinate.y + 1), Side.RIGHT))
        } else {
            neighbors.add(Face(face.coordinate, Side.UP))
        }

        if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z + 1, x = face.coordinate.x + 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z + 1, x = face.coordinate.x + 1), Side.BACK))
        } else if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z + 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z + 1), Side.RIGHT))
        } else {
            neighbors.add(Face(face.coordinate, Side.FRONT))
        }

        if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z - 1, x = face.coordinate.x + 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z - 1, x = face.coordinate.x + 1), Side.FRONT))
        } else if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z - 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z - 1), Side.RIGHT))
        } else {
            neighbors.add(Face(face.coordinate, Side.BACK))
        }
    } else if (face.side == Side.UP) {
        if (coordinates.contains(face.coordinate.copy(y = face.coordinate.y + 1, x = face.coordinate.x + 1))) {
            neighbors.add(Face(face.coordinate.copy(y = face.coordinate.y + 1, x = face.coordinate.x + 1), Side.LEFT))
        } else if (coordinates.contains(face.coordinate.copy(x = face.coordinate.x + 1))) {
            neighbors.add(Face(face.coordinate.copy(x = face.coordinate.x + 1), Side.UP))
        } else {
            neighbors.add(Face(face.coordinate, Side.RIGHT))
        }

        if (coordinates.contains(face.coordinate.copy(y = face.coordinate.y + 1, x = face.coordinate.x - 1))) {
            neighbors.add(Face(face.coordinate.copy(y = face.coordinate.y + 1, x = face.coordinate.x - 1), Side.RIGHT))
        } else if (coordinates.contains(face.coordinate.copy(x = face.coordinate.x - 1))) {
            neighbors.add(Face(face.coordinate.copy(x = face.coordinate.x - 1), Side.UP))
        } else {
            neighbors.add(Face(face.coordinate, Side.LEFT))
        }

        if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z + 1, y = face.coordinate.y + 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z + 1, y = face.coordinate.y + 1), Side.BACK))
        } else if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z + 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z + 1), Side.UP))
        } else {
            neighbors.add(Face(face.coordinate, Side.FRONT))
        }

        if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z - 1, y = face.coordinate.y + 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z - 1, y = face.coordinate.y + 1), Side.FRONT))
        } else if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z - 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z - 1), Side.UP))
        } else {
            neighbors.add(Face(face.coordinate, Side.BACK))
        }
    } else if (face.side == Side.DOWN) {
        if (coordinates.contains(face.coordinate.copy(y = face.coordinate.y - 1, x = face.coordinate.x + 1))) {
            neighbors.add(Face(face.coordinate.copy(y = face.coordinate.y - 1, x = face.coordinate.x + 1), Side.LEFT))
        } else if (coordinates.contains(face.coordinate.copy(x = face.coordinate.x + 1))) {
            neighbors.add(Face(face.coordinate.copy(x = face.coordinate.x + 1), Side.DOWN))
        } else {
            neighbors.add(Face(face.coordinate, Side.RIGHT))
        }

        if (coordinates.contains(face.coordinate.copy(y = face.coordinate.y - 1, x = face.coordinate.x - 1))) {
            neighbors.add(Face(face.coordinate.copy(y = face.coordinate.y - 1, x = face.coordinate.x - 1), Side.RIGHT))
        } else if (coordinates.contains(face.coordinate.copy(x = face.coordinate.x - 1))) {
            neighbors.add(Face(face.coordinate.copy(x = face.coordinate.x - 1), Side.DOWN))
        } else {
            neighbors.add(Face(face.coordinate, Side.LEFT))
        }

        if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z + 1, y = face.coordinate.y - 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z + 1, y = face.coordinate.y - 1), Side.BACK))
        } else if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z + 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z + 1), Side.DOWN))
        } else {
            neighbors.add(Face(face.coordinate, Side.FRONT))
        }

        if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z - 1, y = face.coordinate.y - 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z - 1, y = face.coordinate.y - 1), Side.FRONT))
        } else if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z - 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z - 1), Side.DOWN))
        } else {
            neighbors.add(Face(face.coordinate, Side.BACK))
        }
    } else if (face.side == Side.FRONT) {
        if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z + 1, x = face.coordinate.x + 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z + 1, x = face.coordinate.x + 1), Side.LEFT))
        } else if (coordinates.contains(face.coordinate.copy(x = face.coordinate.x + 1))) {
            neighbors.add(Face(face.coordinate.copy(x = face.coordinate.x + 1), Side.FRONT))
        } else {
            neighbors.add(Face(face.coordinate, Side.RIGHT))
        }

        if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z + 1, x = face.coordinate.x - 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z + 1, x = face.coordinate.x - 1), Side.RIGHT))
        } else if (coordinates.contains(face.coordinate.copy(x = face.coordinate.x - 1))) {
            neighbors.add(Face(face.coordinate.copy(x = face.coordinate.x - 1), Side.FRONT))
        } else {
            neighbors.add(Face(face.coordinate, Side.LEFT))
        }

        if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z + 1, y = face.coordinate.y - 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z + 1, y = face.coordinate.y - 1), Side.UP))
        } else if (coordinates.contains(face.coordinate.copy(y = face.coordinate.y - 1))) {
            neighbors.add(Face(face.coordinate.copy(y = face.coordinate.y - 1), Side.FRONT))
        } else {
            neighbors.add(Face(face.coordinate, Side.DOWN))
        }

        if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z + 1, y = face.coordinate.y + 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z + 1, y = face.coordinate.y + 1), Side.DOWN))
        } else if (coordinates.contains(face.coordinate.copy(y = face.coordinate.y + 1))) {
            neighbors.add(Face(face.coordinate.copy(y = face.coordinate.y + 1), Side.FRONT))
        } else {
            neighbors.add(Face(face.coordinate, Side.UP))
        }
    } else if (face.side == Side.BACK) {
        if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z - 1, x = face.coordinate.x + 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z - 1, x = face.coordinate.x + 1), Side.LEFT))
        } else if (coordinates.contains(face.coordinate.copy(x = face.coordinate.x + 1))) {
            neighbors.add(Face(face.coordinate.copy(x = face.coordinate.x + 1), Side.BACK))
        } else {
            neighbors.add(Face(face.coordinate, Side.RIGHT))
        }

        if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z - 1, x = face.coordinate.x - 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z - 1, x = face.coordinate.x - 1), Side.RIGHT))
        } else if (coordinates.contains(face.coordinate.copy(x = face.coordinate.x - 1))) {
            neighbors.add(Face(face.coordinate.copy(x = face.coordinate.x - 1), Side.BACK))
        } else {
            neighbors.add(Face(face.coordinate, Side.LEFT))
        }

        if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z - 1, y = face.coordinate.y - 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z - 1, y = face.coordinate.y - 1), Side.UP))
        } else if (coordinates.contains(face.coordinate.copy(y = face.coordinate.y - 1))) {
            neighbors.add(Face(face.coordinate.copy(y = face.coordinate.y - 1), Side.BACK))
        } else {
            neighbors.add(Face(face.coordinate, Side.DOWN))
        }

        if (coordinates.contains(face.coordinate.copy(z = face.coordinate.z - 1, y = face.coordinate.y + 1))) {
            neighbors.add(Face(face.coordinate.copy(z = face.coordinate.z - 1, y = face.coordinate.y + 1), Side.DOWN))
        } else if (coordinates.contains(face.coordinate.copy(y = face.coordinate.y + 1))) {
            neighbors.add(Face(face.coordinate.copy(y = face.coordinate.y + 1), Side.BACK))
        } else {
            neighbors.add(Face(face.coordinate, Side.UP))
        }
    }

    return neighbors
}

private data class Face(val coordinate: Coordinate, val side: Side)

private enum class Side {
    LEFT,
    RIGHT,
    UP,
    DOWN,
    FRONT,
    BACK
}
