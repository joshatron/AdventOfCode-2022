package io.joshatron.aoc2022.day

import io.joshatron.aoc2022.readDayInput

fun day04Puzzle01(): String {
    return parseRangePairs(readDayInput(4))
        .filter { oneRangeInAnother(it.range1, it.range2) || oneRangeInAnother(it.range2, it.range1) }
        .size
        .toString()
}

private fun parseRangePairs(input: List<String>): List<RangePair> {
    return input.map(fun(line: String):RangePair {
        val ranges = line.split(",")
        val range1 = ranges[0].split("-")
        val range2 = ranges[1].split("-")

        return RangePair(Range(range1[0].toInt(), range1[1].toInt()), Range(range2[0].toInt(), range2[1].toInt()))
    })
}

private fun oneRangeInAnother(possibleOuter: Range, possibleInner: Range) = possibleOuter.lower <= possibleInner.lower && possibleOuter.upper >= possibleInner.upper

private class RangePair(val range1: Range, val range2: Range)

private class Range(val lower: Int, val upper: Int)

fun day04Puzzle02(): String {
    return parseRangePairs(readDayInput(4))
        .filter { rangesOverlap(it.range1, it.range2) }
        .size
        .toString()
}

private fun rangesOverlap(range1: Range, range2: Range) = numberInRange(range1.lower, range2) || numberInRange(range1.upper, range2) ||
        oneRangeInAnother(range1, range2) || oneRangeInAnother(range2, range1)

private fun numberInRange(num: Int, range: Range) = num >= range.lower && num <= range.upper