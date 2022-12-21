package io.joshatron.aoc2022.days.day20

import io.joshatron.aoc2022.readDayInput

fun day20Puzzle01(): String {
    val list = parseList(readDayInput(20), 1L)
    list.mix(1)
    return list.getCoordinateSum().toString()
}

private fun parseList(input: List<String>, multiplier: Long): CircularList {
    val circularList = CircularList(input[0].toInt() * multiplier)

    for (line in input.subList(1, input.size)) {
        circularList.addToEnd(line.toInt() * multiplier)
    }

    return circularList
}

private class CircularList(initial: Long) {
    val start = CircularListElement(previous = null, next = null, value = initial)
    var size = 1

    init {
        start.previous = start
        start.next = start
    }

    fun addToEnd(value: Long) {
        val newElement = CircularListElement(previous = start.previous, next = start, value = value)
        start.previous!!.next = newElement
        start.previous = newElement
        size++
    }

    fun mix(times: Int) {
        val elementsToMix: MutableList<CircularListElement> = ArrayList()
        var current = start
        repeat(size) {
            elementsToMix.add(current)
            current = current.next!!
        }

        repeat (times) {
            for (element in elementsToMix) {
                if (element.value == 0L) {
                    continue
                }
                element.next!!.previous = element.previous
                element.previous!!.next = element.next
                var toInsertAfter = element
                if (element.value > 0) {
                    for (i in 0 until element.value % (size - 1)) {
                        toInsertAfter = toInsertAfter.next!!
                    }
                } else {
                    toInsertAfter = toInsertAfter.previous!!
                    for (i in 0 until (element.value * -1) % (size - 1)) {
                        toInsertAfter = toInsertAfter.previous!!
                    }
                }
                element.next = toInsertAfter.next
                element.previous = toInsertAfter
                toInsertAfter.next!!.previous = element
                toInsertAfter.next = element
            }
        }
    }

    fun getCoordinateSum(): Long {
        var current = start
        while (current.value != 0L) {
            current = current.next!!
        }

        var total = 0L
        repeat (3) {
            repeat(1000 % size) {
                current = current.next!!
            }
            total += current.value
        }

        return total
    }
}

private class CircularListElement(var previous: CircularListElement?, var next: CircularListElement?, val value: Long)

fun day20Puzzle02(): String {
    val list = parseList(readDayInput(20), 811589153L)
    list.mix(10)
    return list.getCoordinateSum().toString()
}
