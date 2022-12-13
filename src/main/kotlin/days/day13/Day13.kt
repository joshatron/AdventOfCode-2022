package io.joshatron.aoc2022.days.day13

import io.joshatron.aoc2022.readDayInput

fun day13Puzzle01(): String {
    val packetPairs = parsePackets(readDayInput(13))
    var total = 0
    for (i in packetPairs.indices) {
        if (isPairValid(packetPairs[i])) {
            total += i + 1
        }
    }
    return total.toString()
}

private fun isPairValid(packetPair: PacketPair): Boolean {
    return isListOrNumbersValid(packetPair.left, packetPair.right) == InOrder.TRUE
}

private fun isListOrNumbersValid(left: ListOrNumber, right: ListOrNumber): InOrder {
    if (left.isNumber && right.isNumber) {
        return when {
            left.number!! > right.number!! -> InOrder.FALSE
            left.number < right.number -> InOrder.TRUE
            else -> InOrder.UNKNOWN
        }
    } else if (left.isNumber) {
        val listedLeft = ListOrNumber(listOf(left), null)
        return isListOrNumbersValid(listedLeft, right)
    } else if (right.isNumber) {
        val listedRight = ListOrNumber(listOf(right), null)
        return isListOrNumbersValid(left, listedRight)
    } else {
        val leftList = left.list!!
        val rightList = right.list!!
        for (i in leftList.indices) {
            if (rightList.size <= i) {
                return InOrder.FALSE
            }
            val comparison = isListOrNumbersValid(leftList[i], rightList[i])
            if (comparison != InOrder.UNKNOWN) {
                return comparison
            }
        }
        return if (leftList.size < rightList.size) {
            InOrder.TRUE
        } else {
            InOrder.UNKNOWN
        }
    }
}

private enum class InOrder {
    TRUE,
    FALSE,
    UNKNOWN
}

private fun parsePackets(input: List<String>): List<PacketPair> {
    val packetPairs: MutableList<PacketPair> = ArrayList()
    var i = 0
    while (i < input.size) {
        val left = parseListOrNumber(input[i])
        i++
        val right = parseListOrNumber(input[i])
        packetPairs.add(PacketPair(left, right))
        i += 2
    }

    return packetPairs
}

private fun parseListOrNumber(line: String): ListOrNumber {
    if (line.startsWith("[")) {
        val elements: MutableList<ListOrNumber> = ArrayList()
        var level = 0
        var current = ""
        for (i in 1 until line.length-1) {
            if (level == 0 && line[i] == ',') {
                elements.add(parseListOrNumber(current))
                current = ""
            } else {
                current += line[i]
                if (line[i] == '[') {
                    level++
                } else if (line[i] == ']') {
                    level--
                }
            }
        }
        if (current.isNotEmpty()) {
            elements.add(parseListOrNumber(current))
        }

        return ListOrNumber(elements, null)
    } else {
        return ListOrNumber(null, line.toInt())
    }
}

private data class PacketPair(val left: ListOrNumber, val right: ListOrNumber)

private data class ListOrNumber(val list: List<ListOrNumber>?, val number: Int?) {
    val isList = list != null
    val isNumber = number != null
}

fun day13Puzzle02(): String {
    val packets = packetPairsToPacketList(parsePackets(readDayInput(13)))
    val firstDivider = ListOrNumber(listOf(ListOrNumber(null, 2)), null)
    val secondDivider = ListOrNumber(listOf(ListOrNumber(null, 6)), null)
    packets.add(firstDivider)
    packets.add(secondDivider)

    packets.sortWith(PacketComparator)

    var total = 1
    for (i in packets.indices) {
        if (packets[i] == firstDivider || packets[i] == secondDivider)  {
            total *= (i + 1)
        }
    }

    return total.toString()
}

private class PacketComparator {
    companion object : Comparator<ListOrNumber> {
        override fun compare(a: ListOrNumber, b: ListOrNumber): Int = when (isListOrNumbersValid(a, b)) {
            InOrder.TRUE -> -1
            InOrder.FALSE -> 1
            InOrder.UNKNOWN -> 0
        }
    }
}

private fun packetPairsToPacketList(pairs: List<PacketPair>): MutableList<ListOrNumber> {
    val packets: MutableList<ListOrNumber> = ArrayList()

    for (pair in pairs) {
        packets.add(pair.left)
        packets.add(pair.right)
    }

    return packets
}
