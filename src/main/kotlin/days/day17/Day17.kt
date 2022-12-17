package io.joshatron.aoc2022.days.day17

import io.joshatron.aoc2022.readDayInput
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.math.max

fun day17Puzzle01(): String {
    val chamber = Chamber(parseJetPattern(readDayInput(17)))
    repeat(2022) {
        chamber.addBlock()
    }
    return chamber.topElevation.toString()
}

private fun parseJetPattern(input: List<String>): List<Direction> {
    return input[0].map { if (it == '<') Direction.LEFT else Direction.RIGHT }
}

private class Chamber(private val jetPattern: List<Direction>) {
    val chamber: MutableSet<Coordinate> = HashSet()
    val width = 7
    var nextBlock = 0
    var topElevation = 0
    var jetStep = 0

    fun draw() {
        for (y in topElevation downTo 1) {
            print("|")
            for (x in 0 until width) {
                if (chamber.contains(Coordinate(x,y))) {
                    print("█")
                } else {
                    print(".")
                }
            }
            println("|")
        }
        println("---------")
    }

    fun addBlock() {
        var newBlocks = getBlockInitialCoords()
        while (true) {
            if (canMoveInDirection(newBlocks, jetPattern[jetStep])) {
                newBlocks = moveInDirection(newBlocks, jetPattern[jetStep])
            }
            jetStep = (jetStep + 1) % jetPattern.size

            if (canMoveInDirection(newBlocks, Direction.DOWN)) {
                newBlocks = moveInDirection(newBlocks, Direction.DOWN)
            } else {
                chamber.addAll(newBlocks)
                topElevation = max(topElevation, newBlocks.fold(0) { acc, c -> max(acc, c.y) })
                break
            }
        }
    }

    private fun moveInDirection(blocks: List<Coordinate>, direction: Direction): List<Coordinate> {
        val newBlockLocs: MutableList<Coordinate> = ArrayList()
        for (block in blocks) {
            when (direction) {
                Direction.LEFT -> newBlockLocs.add(block.copy(x = block.x - 1))
                Direction.RIGHT -> newBlockLocs.add(block.copy(x = block.x + 1))
                Direction.DOWN -> newBlockLocs.add(block.copy(y = block.y - 1))
                else -> continue
            }
        }

        return newBlockLocs
    }

    private fun canMoveInDirection(blocks: List<Coordinate>, direction: Direction):Boolean {
        for (block in blocks) {
            when (direction) {
                Direction.LEFT -> if (chamber.contains(block.copy(x = block.x - 1)) || block.x <= 0) return false
                Direction.RIGHT -> if (chamber.contains(block.copy(x = block.x + 1)) || block.x >= width-1) return false
                Direction.DOWN -> if (chamber.contains(block.copy(y = block.y - 1)) || block.y <= 1) return false
                else -> continue
            }
        }

        return true
    }

    private fun getBlockInitialCoords(): List<Coordinate> {
        val blocks = when (nextBlock) {
            0 -> listOf(Coordinate(2,topElevation+4), Coordinate(3, topElevation+4), Coordinate(4, topElevation+4), Coordinate(5, topElevation+4))
            1 -> listOf(Coordinate(2,topElevation+5), Coordinate(3, topElevation+5), Coordinate(4, topElevation+5), Coordinate(3, topElevation+4), Coordinate(3, topElevation+6))
            2 -> listOf(Coordinate(2,topElevation+4), Coordinate(3, topElevation+4), Coordinate(4, topElevation+4), Coordinate(4, topElevation+5), Coordinate(4, topElevation+6))
            3 -> listOf(Coordinate(2,topElevation+4), Coordinate(2, topElevation+5), Coordinate(2, topElevation+6), Coordinate(2, topElevation+7))
            4 -> listOf(Coordinate(2,topElevation+4), Coordinate(3, topElevation+4), Coordinate(2, topElevation+5), Coordinate(3, topElevation+5))
            else -> listOf()
        }

        nextBlock = (nextBlock + 1) % 5

        return blocks
    }
}

private data class Coordinate(val x: Int, val y: Int)

private enum class Direction {
    LEFT,
    RIGHT,
    DOWN,
    UP,
    NONE
}

fun day17Puzzle02(): String {
    val chamber = Chamber(parseJetPattern(readDayInput(17)))
    val statuses: MutableSet<ChamberStatus> = HashSet()
    val increases: MutableList<Int> = ArrayList()
    var lastElevation = 0
    for ( i in 0..100000) {
        chamber.addBlock()
        increases.add(chamber.topElevation - lastElevation)
        lastElevation = chamber.topElevation
        if ((chamber.nextBlock == 0 && increases.last() == 2) || (chamber.nextBlock == 1 && increases.last() == 1) ||
            (chamber.nextBlock == 2 && increases.last() == 3) || (chamber.nextBlock == 3 && increases.last() == 3) ||
            (chamber.nextBlock == 4 && increases.last() == 4)) {
            val topRow: MutableList<Boolean> = ArrayList()
            for (x in 0 until chamber.width) {
                topRow.add(chamber.chamber.contains(Coordinate(x, chamber.topElevation)))
            }
            val status = ChamberStatus(chamber.nextBlock, chamber.jetStep, topRow, i, chamber.topElevation)
            if (statuses.contains(status)) {
                val previousStatus =
                    statuses.find { it.nextBlock == status.nextBlock && it.nextJetStep == status.nextJetStep && it.topRow == status.topRow }!!
                val increase = status.topElevation - previousStatus.topElevation
                val initial = previousStatus.topElevation
                val stepsIncrease = status.step - previousStatus.step
                val stepsInitial = previousStatus.step

                return calculateAtStepX(
                    1000000000000L,
                    stepsInitial,
                    stepsIncrease,
                    initial,
                    increase,
                    increases
                ).toString()
            } else {
                statuses.add(status)
            }
        }
    }
    return ""
}

private fun calculateAtStepX(x: Long, initialStep: Int, stepJump: Int, initialElevation: Int, stepElevation: Int, elevationIncreases: List<Int>): Long {
    val toHopUp = (x / stepJump) - 1
    var current = initialElevation + toHopUp * stepElevation
    var currentStep = initialStep + toHopUp * stepJump + 1

    var increaseIndex = 1
    while (currentStep < x) {
        current += elevationIncreases[increaseIndex + initialStep]
        increaseIndex = (increaseIndex + 1) % stepJump
        currentStep++
    }

    return current
}

private data class ChamberStatus(val nextBlock: Int, val nextJetStep: Int, val topRow: List<Boolean>, val step: Int, val topElevation: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as ChamberStatus

        if (nextBlock != other.nextBlock) return false
        if (nextJetStep != other.nextJetStep) return false
        if (topRow != other.topRow) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(nextBlock, nextJetStep, topRow)
    }
}