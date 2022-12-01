package io.joshatron.aoc2022

fun readDayInput(day: Int): List<String> =
    object {}::class.java.getResource(getFileName(day))?.readText(Charsets.UTF_8)?.split("\n") ?: ArrayList()

private fun getFileName(day: Int): String = "/day${day.toString().padStart(2, '0')}.txt"
