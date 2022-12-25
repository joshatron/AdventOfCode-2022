package io.joshatron.aoc2022.days.day25

import io.joshatron.aoc2022.readDayInput
import kotlin.math.pow
import kotlin.math.round

fun day25Puzzle01(): String {
    val total = readDayInput(25).map { snafuToDecimal(it) }.fold(0L) { acc, n -> acc + n }
    return decimalToSnafu(total)
}

private fun snafuToDecimal(snafu: String): Long {
    var total = 0L
    for (i in snafu.indices) {
        total += when (snafu[i]) {
            '2' -> round(2 * 5.0.pow((snafu.length - i - 1).toDouble())).toLong()
            '1' -> round(5.0.pow((snafu.length - i - 1).toDouble())).toLong()
            '-' -> round(-1 * 5.0.pow((snafu.length - i - 1).toDouble())).toLong()
            '=' -> round(-2 * 5.0.pow((snafu.length - i - 1).toDouble())).toLong()
            else -> 0
        }
    }

    return total
}

private fun decimalToSnafu(decimal: Long): String {
    var snafu = decimal.toString(5)

    for (i in snafu.indices.reversed()) {
        if (snafu[i] == '3') {
            snafu = "${snafu.subSequence(0,i)}=${snafu.subSequence(i+1,snafu.length)}"
            snafu = carryBase5(snafu, i)
        } else if (snafu[i] == '4') {
            snafu = "${snafu.subSequence(0,i)}-${snafu.subSequence(i+1,snafu.length)}"
            snafu = carryBase5(snafu, i)
        }
    }

    return snafu
}

private fun carryBase5(original: String, place: Int): String {
    if (place == 0) {
        return "1$original"
    } else {
        var newStr = original
        var i = place - 1
        while (true) {
            if (i < 0) {
                return "1$newStr"
            }
            if (original[i] == '4') {
                newStr = "${newStr.subSequence(0,i)}0${newStr.subSequence(i+1,newStr.length)}"
                i--
            } else {
                return "${newStr.subSequence(0,i)}${newStr[i].digitToInt() + 1}${newStr.subSequence(i+1,newStr.length)}"
            }
        }
    }
}

fun day25Puzzle02(): String {
    return "Merry Christmas!"
}