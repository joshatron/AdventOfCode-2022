package io.joshatron.aoc2022.days.day14

import io.joshatron.aoc2022.readDayInput

fun day14Puzzle01(): String {
    val cave = parseCave(readDayInput(14))
    var sandPieces = 0
    var done = false
    while (!done) {
        var newSand = cave.sandStart.copy()
        while(true) {
            var possibleNext = newSand.copy(y = newSand.y + 1)
            if (!cave.inBounds(possibleNext)) {
                done = true
                break
            } else if (cave.getAtLoc(possibleNext) == CaveLocationType.AIR) {
                newSand = possibleNext
                continue
            }
            possibleNext = newSand.copy(x = newSand.x - 1,y = newSand.y + 1)
            if (!cave.inBounds(possibleNext)) {
                done = true
                break
            } else if (cave.getAtLoc(possibleNext) == CaveLocationType.AIR) {
                newSand = possibleNext
                continue
            }
            possibleNext = newSand.copy(x = newSand.x + 1,y = newSand.y + 1)
            if (!cave.inBounds(possibleNext)) {
                done = true
                break
            } else if (cave.getAtLoc(possibleNext) == CaveLocationType.AIR) {
                newSand = possibleNext
                continue
            }
            break
        }

        if (!done) {
            cave.setAtLoc(newSand, CaveLocationType.SAND)
            sandPieces++
        }
    }

    return sandPieces.toString()
}

private fun parseCave(input: List<String>): Cave {
    val paths = input.map { line -> line.split(" ")
        .filter { it != "->" }
        .map { Coordinate(it.split(",")[0].toInt(), it.split(",")[1].toInt()) }
    }
    val minX = paths.map { it.reduce { min, cur -> if (cur.x < min.x) cur else min } }
        .reduce { min, cur -> if (cur.x < min.x) cur else min }.x
    val maxX = paths.map { it.reduce { max, cur -> if (cur.x > max.x) cur else max } }
        .reduce { max, cur -> if (cur.x > max.x) cur else max }.x
    val maxY = paths.map { it.reduce { max, cur -> if (cur.y > max.y) cur else max } }
        .reduce { max, cur -> if (cur.y > max.y) cur else max }.y

    val caveCoords: MutableList<MutableList<CaveLocationType>> = ArrayList()
    repeat (maxY + 1) {
        val row: MutableList<CaveLocationType> = ArrayList()
        repeat (maxX - minX + 1) {
            row.add(CaveLocationType.AIR)
        }
        caveCoords.add(row)
    }

    val cave = Cave(caveCoords, Coordinate(500 - minX, 0))

    for (path in paths) {
        for (i in 0 until path.size - 1) {
            var current = path[i]
            while (current != path[i+1]) {
                cave.setAtLoc(current.copy(x = current.x - minX), CaveLocationType.ROCK)
                current = moveTowardCoord(current, path[i+1])
            }
            cave.setAtLoc(path[i+1].copy(x = current.x - minX), CaveLocationType.ROCK)
        }
    }

    return cave
}

private fun moveTowardCoord(current: Coordinate, target: Coordinate): Coordinate {
    return if (current.x < target.x) {
        current.copy(x = current.x + 1)
    } else if (current.x > target.x) {
        current.copy(x = current.x - 1)
    } else if (current.y < target.y) {
        current.copy(y = current.y + 1)
    } else if (current.y > target.y) {
        current.copy(y = current.y - 1)
    } else {
        current
    }
}

private class Cave(val cave: MutableList<MutableList<CaveLocationType>>, var sandStart: Coordinate) {
    fun setAtLoc(coordinate: Coordinate, type: CaveLocationType) {
        cave[coordinate.y][coordinate.x] = type
    }

    fun getAtLoc(coordinate: Coordinate): CaveLocationType {
        return cave[coordinate.y][coordinate.x]
    }

    fun inBounds(coordinate: Coordinate): Boolean {
        return coordinate.x >= 0 && coordinate.x < cave[0].size && coordinate.y >= 0 && coordinate.y < cave.size
    }
}

private data class Coordinate(var x: Int, val y: Int)

private enum class CaveLocationType {
    ROCK,
    SAND,
    AIR
}

fun day14Puzzle02(): String {
    val cave = transformCaveForPuzzle02(parseCave(readDayInput(14)))
    var sandPieces = 0
    var done = false
    while (!done) {
        var newSand = cave.sandStart.copy()
        while(true) {
            var possibleNext = newSand.copy(y = newSand.y + 1)
            if (cave.getAtLoc(possibleNext) == CaveLocationType.AIR) {
                newSand = possibleNext
                continue
            }
            possibleNext = newSand.copy(x = newSand.x - 1,y = newSand.y + 1)
            if (cave.getAtLoc(possibleNext) == CaveLocationType.AIR) {
                newSand = possibleNext
                continue
            }
            possibleNext = newSand.copy(x = newSand.x + 1,y = newSand.y + 1)
            if (cave.getAtLoc(possibleNext) == CaveLocationType.AIR) {
                newSand = possibleNext
                continue
            }
            break
        }

        cave.setAtLoc(newSand, CaveLocationType.SAND)
        sandPieces++

        if (newSand == cave.sandStart) {
            done = true
        }
    }

    return sandPieces.toString()
}

private fun transformCaveForPuzzle02(cave: Cave): Cave {
    val height = cave.cave.size + 2
    for (row in cave.cave) {
        for (i in 0 until height) {
            row.add(0, CaveLocationType.AIR)
            row.add(CaveLocationType.AIR)
        }
    }
    val secondFromBottomRow: MutableList<CaveLocationType> = ArrayList()
    val bottomRow: MutableList<CaveLocationType> = ArrayList()
    for (i in 0 until cave.cave[0].size) {
        secondFromBottomRow.add(CaveLocationType.AIR)
        bottomRow.add(CaveLocationType.ROCK)
    }
    cave.cave.add(secondFromBottomRow)
    cave.cave.add(bottomRow)

    cave.sandStart.x = cave.sandStart.x + height

    return cave
}