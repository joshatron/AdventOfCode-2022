package io.joshatron.aoc2022.day

import io.joshatron.aoc2022.readDayInput
import kotlin.streams.toList

fun day08Puzzle01(): String {
    val forest = parseForest(readDayInput(8))
    return countVisibleTrees(forest).toString()
}

private fun countVisibleTrees(forest: Forest): Int {
    var total = 0
    for (x in 0 until forest.width) {
        for (y in 0 until forest.height) {
            if (isTreeVisible(forest, x, y)) {
                total++
            }
        }
    }

    return total
}

private fun isTreeVisible(forest: Forest, x: Int, y: Int): Boolean {
    var clear = true
    for (tx in (x-1) downTo 0) {
        if (forest.heightAtIndex(tx, y) >= forest.heightAtIndex(x, y)) {
            clear = false
            break
        }
    }
    if (clear) {
        return true
    }
    clear = true
    for (tx in (x+1) until forest.width) {
        if (forest.heightAtIndex(tx, y) >= forest.heightAtIndex(x, y)) {
            clear = false
            break
        }
    }
    if (clear) {
        return true
    }
    clear = true
    for (ty in (y-1) downTo 0) {
        if (forest.heightAtIndex(x, ty) >= forest.heightAtIndex(x, y)) {
            clear = false
            break
        }
    }
    if (clear) {
        return true
    }
    clear = true
    for (ty in (y+1) until forest.height) {
        if (forest.heightAtIndex(x, ty) >= forest.heightAtIndex(x, y)) {
            clear = false
            break
        }
    }
    if (clear) {
        return true
    }
    return false
}

private fun parseForest(input: List<String>): Forest {
    val treeHeights = ArrayList<List<Int>>()
    for (line in input) {
        treeHeights.add(line.chars().map { it - 48 }.toList())
    }

    return Forest(treeHeights)
}

private class Forest(val treeHeights: List<List<Int>>) {
    val width = treeHeights[0].size
    val height = treeHeights.size

    fun heightAtIndex(x: Int, y: Int): Int {
        return treeHeights[y][x]
    }
}

fun day08Puzzle02(): String {
    val forest = parseForest(readDayInput(8))
    return findBestVisibility(forest).toString()
}

private fun findBestVisibility(forest: Forest): Int {
    var max = 0
    for (x in 1 until forest.width-1) {
        for (y in 1 until forest.height-1) {
            val visibility = determineVisibility(forest, x, y)
            if (visibility > max) {
                max = visibility
            }
        }
    }

    return max
}

private fun determineVisibility(forest: Forest, x: Int, y: Int): Int {
    var score = 1

    for (tx in (x-1) downTo 0) {
        if (forest.heightAtIndex(tx, y) >= forest.heightAtIndex(x, y) || tx == 0) {
            score *= (x-tx)
            break
        }
    }
    for (tx in (x+1) until forest.width) {
        if (forest.heightAtIndex(tx, y) >= forest.heightAtIndex(x, y) || tx == forest.width-1) {
            score *= (tx-x)
            break
        }
    }
    for (ty in (y-1) downTo 0) {
        if (forest.heightAtIndex(x, ty) >= forest.heightAtIndex(x, y) || ty == 0) {
            score *= (y-ty)
            break
        }
    }
    for (ty in (y+1) until forest.height) {
        if (forest.heightAtIndex(x, ty) >= forest.heightAtIndex(x, y) || ty == forest.height-1) {
            score *= (ty-y)
            break
        }
    }

    return score
}
