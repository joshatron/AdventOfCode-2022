package io.joshatron.aoc2022.days.day22

import io.joshatron.aoc2022.readDayInput
import kotlin.math.max

fun day22Puzzle01(): String {
    val passwordMap = parseMap(readDayInput(22))
    val instructions = parseInstructions(readDayInput(22))

    var currentCoordinate = passwordMap.findStart()
    var currentDirection = Direction.RIGHT

    for (instruction in instructions) {
        if (instruction.distance != null) {
            repeat(instruction.distance) {
                currentCoordinate = passwordMap.move(currentCoordinate, currentDirection)
            }
        } else {
            currentDirection = rotateDirection(currentDirection, instruction.turn!!)
        }
    }

    return calculatePassword(currentCoordinate, currentDirection).toString()
}

private fun calculatePassword(coordinate: Coordinate, direction: Direction): Int {
    var total = 1000 * coordinate.y + 4 * coordinate.x
    total += when (direction) {
        Direction.UP -> 3
        Direction.DOWN -> 1
        Direction.RIGHT -> 0
        Direction.LEFT -> 2
    }

    return total
}

private fun rotateDirection(direction: Direction, rotation: TurnType): Direction {
    return when (rotation) {
        TurnType.COUNTERCLOCKWISE -> when (direction) {
            Direction.UP -> Direction.LEFT
            Direction.DOWN -> Direction.RIGHT
            Direction.RIGHT -> Direction.UP
            Direction.LEFT -> Direction.DOWN
        }
        TurnType.CLOCKWISE -> when (direction) {
            Direction.UP -> Direction.RIGHT
            Direction.DOWN -> Direction.LEFT
            Direction.RIGHT -> Direction.DOWN
            Direction.LEFT -> Direction.UP
        }
    }
}

private fun parseInstructions(input: List<String>): List<Instruction> {
    val lastLine = input.last()
    val instructions: MutableList<Instruction> = ArrayList()

    var current = ""
    for (c in lastLine) {
        when (c) {
            'L' -> {
                if (current.isNotEmpty()) {
                    instructions.add(Instruction(current.toInt(), null))
                    current = ""
                }
                instructions.add(Instruction(null, TurnType.COUNTERCLOCKWISE))
            }
            'R' -> {
                if (current.isNotEmpty()) {
                    instructions.add(Instruction(current.toInt(), null))
                    current = ""
                }
                instructions.add(Instruction(null, TurnType.CLOCKWISE))
            }
            else -> {
                current += c
            }
        }
    }

    if (current.isNotEmpty()) {
        instructions.add(Instruction(current.toInt(), null))
    }

    return instructions
}

private class Instruction(val distance: Int?, val turn: TurnType?)

private enum class TurnType {
    COUNTERCLOCKWISE,
    CLOCKWISE
}

private fun parseMap(input: List<String>): PasswordMap {
    val width = input.subList(0, input.size - 2).fold(0) { acc, line -> max(acc, line.length + 2) }
    val coordinates: MutableList<List<MapCoordinateType>> = ArrayList()

    val firstLine: MutableList<MapCoordinateType> = ArrayList()
    repeat (width + 2) {
        firstLine.add(MapCoordinateType.PORTAL)
    }
    coordinates.add(firstLine)
    for (line in input) {
        if (line.isEmpty()) {
            break
        }

        val coordinateLine: MutableList<MapCoordinateType> = ArrayList()
        coordinateLine.add(MapCoordinateType.PORTAL)
        for (i in 0 until width) {
            if (i < line.length) {
                when (line[i]) {
                    '.' -> coordinateLine.add(MapCoordinateType.EMPTY)
                    '#' -> coordinateLine.add(MapCoordinateType.WALL)
                    else -> coordinateLine.add(MapCoordinateType.PORTAL)
                }
            } else {
                coordinateLine.add(MapCoordinateType.PORTAL)
            }
        }
        coordinateLine.add(MapCoordinateType.PORTAL)
        coordinates.add(coordinateLine)
    }
    val lastLine: MutableList<MapCoordinateType> = ArrayList()
    repeat (width + 2) {
        lastLine.add(MapCoordinateType.PORTAL)
    }
    coordinates.add(lastLine)

    return PasswordMap(coordinates)
}

private class PasswordMap(val coordinates: List<List<MapCoordinateType>>) {
    fun findStart(): Coordinate {
        for (i in 0 until coordinates[1].size) {
            if (coordinates[1][i] == MapCoordinateType.EMPTY) {
                return Coordinate(i, 1)
            }
        }
        return Coordinate(1,1)
    }

    fun move(start: Coordinate, direction: Direction): Coordinate {
        var coordinate = when (direction) {
            Direction.UP -> start.copy(y = start.y - 1)
            Direction.DOWN -> start.copy(y = start.y + 1)
            Direction.RIGHT -> start.copy(x = start.x + 1)
            Direction.LEFT -> start.copy(x = start.x - 1)
        }
        if (coordinates[coordinate.y][coordinate.x] == MapCoordinateType.WALL) {
            coordinate = start
        } else if (coordinates[coordinate.y][coordinate.x] == MapCoordinateType.PORTAL) {
            var tempCoordinate = when (direction) {
                Direction.UP -> coordinate.copy(y = coordinates.size - 1)
                Direction.DOWN -> coordinate.copy(y = 0)
                Direction.RIGHT -> coordinate.copy(x = 0)
                Direction.LEFT -> coordinate.copy(x = coordinates[0].size - 1)
            }
            while (coordinates[tempCoordinate.y][tempCoordinate.x] == MapCoordinateType.PORTAL) {
                tempCoordinate = when (direction) {
                    Direction.UP -> tempCoordinate.copy(y = tempCoordinate.y - 1)
                    Direction.DOWN -> tempCoordinate.copy(y = tempCoordinate.y + 1)
                    Direction.RIGHT -> tempCoordinate.copy(x = tempCoordinate.x + 1)
                    Direction.LEFT -> tempCoordinate.copy(x = tempCoordinate.x - 1)
                }
            }
            coordinate = if (coordinates[tempCoordinate.y][tempCoordinate.x] == MapCoordinateType.EMPTY) {
                tempCoordinate
            } else {
                start
            }
        }

        return coordinate
    }

    fun moveCubed(heading: Heading): Heading {
        var coordinate = when (heading.direction) {
            Direction.UP -> heading.coordinate.copy(y = heading.coordinate.y - 1)
            Direction.DOWN -> heading.coordinate.copy(y = heading.coordinate.y + 1)
            Direction.RIGHT -> heading.coordinate.copy(x = heading.coordinate.x + 1)
            Direction.LEFT -> heading.coordinate.copy(x = heading.coordinate.x - 1)
        }
        var direction = heading.direction
        if (coordinates[coordinate.y][coordinate.x] == MapCoordinateType.PORTAL) {
            if (coordinate.y == 0 && coordinate.x <= 100) {
                coordinate = Coordinate(1, 151 + (coordinate.x-51))
                direction = Direction.RIGHT
            } else if (coordinate.y == 0) {
                coordinate = Coordinate(coordinate.x-100, 200)
            } else if (coordinate.y <= 50 && coordinate.x == 50) {
                coordinate = Coordinate(1, 151 - coordinate.y)
                direction = Direction.RIGHT
            } else if (coordinate.y <= 50 && coordinate.x == 151) {
                coordinate = Coordinate(100, 151 - coordinate.y)
                direction = Direction.LEFT
            } else if (coordinate.y == 51 && direction == Direction.DOWN) {
                coordinate = Coordinate(100, 51 + (coordinate.x-101))
                direction = Direction.LEFT
            } else if (coordinate.y <= 100 && coordinate.x == 50 && direction == Direction.LEFT) {
                coordinate = Coordinate(coordinate.y - 50, 101)
                direction = Direction.DOWN
            } else if (coordinate.y <= 100 && coordinate.x == 101) {
                coordinate = Coordinate(101 + (coordinate.y - 51), 50)
                direction = Direction.UP
            } else if (coordinate.y == 100 && direction == Direction.UP) {
                coordinate = Coordinate(51, coordinate.x + 50)
                direction = Direction.RIGHT
            } else if (coordinate.y <= 150 && direction == Direction.LEFT) {
                coordinate = Coordinate(51, 50 - (coordinate.y-101))
                direction = Direction.RIGHT
            } else if (coordinate.y <= 150) {
                coordinate = Coordinate(150, 50 - (coordinate.y-101))
                direction = Direction.LEFT
            } else if (coordinate.y == 151 && direction == Direction.DOWN) {
                coordinate = Coordinate(50, 151 + (coordinate.x-51))
                direction = Direction.LEFT
            } else if (coordinate.y <= 200 && direction == Direction.LEFT) {
                coordinate = Coordinate(51 + (coordinate.y - 151), 1)
                direction = Direction.DOWN
            } else if (coordinate.y <= 200) {
                coordinate = Coordinate(51 + (coordinate.y - 151), 150)
                direction = Direction.UP
            } else if (coordinate.y == 201) {
                coordinate = Coordinate(100 + coordinate.x, 1)
                direction = Direction.DOWN
            } else {
                println("Something went wrong: $coordinate, $direction")
            }
        }

        return if (coordinates[coordinate.y][coordinate.x] == MapCoordinateType.WALL) {
            heading
        } else {
            Heading(coordinate, direction)
        }
    }
}

private enum class MapCoordinateType {
    EMPTY,
    WALL,
    PORTAL
}

private data class Coordinate(val x: Int, val y: Int)

private enum class Direction {
    UP,
    DOWN,
    RIGHT,
    LEFT
}

fun day22Puzzle02(): String {
    val passwordMap = parseMap(readDayInput(22))
    val instructions = parseInstructions(readDayInput(22))

    var currentHeading = Heading(passwordMap.findStart(), Direction.RIGHT)

    for (instruction in instructions) {
        if (instruction.distance != null) {
            repeat(instruction.distance) {
                currentHeading = passwordMap.moveCubed(currentHeading)
            }
        } else {
            currentHeading = currentHeading.copy(direction = rotateDirection(currentHeading.direction, instruction.turn!!))
        }
    }

    return calculatePassword(currentHeading.coordinate, currentHeading.direction).toString()
}

private data class Heading(val coordinate: Coordinate, val direction: Direction)