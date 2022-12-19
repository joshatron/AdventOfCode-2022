package io.joshatron.aoc2022.days.day19

import io.joshatron.aoc2022.readDayInput
import java.util.*
import kotlin.math.max

fun day19Puzzle01(): String {
    val blueprints = parseBlueprints(readDayInput(19))
    return blueprints.fold(0) { acc, b -> acc + (getBlueprintMaxProduced(b, 24) * b.number) }.toString()
}

private fun getBlueprintMaxProduced(blueprint: Blueprint, minutes: Int): Int {
    val maxOreCost = max(blueprint.oreRobotOreCost, max(blueprint.clayRobotOreCost, max(blueprint.obsidianRobotOreCost, blueprint.geodeRobotOreCost)))
    val miningStates: Queue<MiningState> = LinkedList()
    miningStates.add(MiningState())
    var maxProduced = 0
    var maxGeodeRobots = 0
    while (miningStates.isNotEmpty()) {
        val currentState = miningStates.remove()
        if (currentState.ore < 0 || currentState.clay < 0 || currentState.obsidian < 0) {
            println(currentState)
        }
        if (currentState.minute == minutes) {
            maxProduced = max(maxProduced, currentState.geodesCracked)
            continue
        }
        if (currentState.geodeRobots + 1 < maxGeodeRobots) {
            continue
        }

        if (currentState.ore >= blueprint.geodeRobotOreCost && currentState.obsidian >= blueprint.geodeRobotObsidianCost) {
            miningStates.add(currentState.copy(minute = currentState.minute + 1,
                geodeRobots = currentState.geodeRobots + 1,
                ore = currentState.ore + currentState.oreRobots - blueprint.geodeRobotOreCost,
                clay = currentState.clay + currentState.clayRobots,
                obsidian = currentState.obsidian + currentState.obsidianRobots - blueprint.geodeRobotObsidianCost,
                geodesCracked = currentState.geodesCracked + currentState.geodeRobots,))
            maxGeodeRobots = max(maxGeodeRobots, currentState.geodeRobots + 1)
        } else if (currentState.ore >= blueprint.obsidianRobotOreCost && currentState.clay >= blueprint.obsidianRobotClayCost) {
            miningStates.add(currentState.copy(minute = currentState.minute + 1,
                obsidianRobots = currentState.obsidianRobots + 1,
                ore = currentState.ore + currentState.oreRobots - blueprint.obsidianRobotOreCost,
                clay = currentState.clay + currentState.clayRobots - blueprint.obsidianRobotClayCost,
                obsidian = currentState.obsidian + currentState.obsidianRobots,
                geodesCracked = currentState.geodesCracked + currentState.geodeRobots,))
            miningStates.add(currentState.copy(minute = currentState.minute + 1,
                ore = currentState.ore + currentState.oreRobots,
                clay = currentState.clay + currentState.clayRobots,
                obsidian = currentState.obsidian + currentState.obsidianRobots,
                geodesCracked = currentState.geodesCracked + currentState.geodeRobots,))
        } else if ((currentState.ore >= blueprint.clayRobotOreCost && currentState.clayRobots < blueprint.obsidianRobotClayCost) &&
                   (currentState.ore >= blueprint.oreRobotOreCost && currentState.oreRobots < maxOreCost)) {
            miningStates.add(currentState.copy(minute = currentState.minute + 1,
                clayRobots = currentState.clayRobots + 1,
                ore = currentState.ore + currentState.oreRobots - blueprint.clayRobotOreCost,
                clay = currentState.clay + currentState.clayRobots,
                obsidian = currentState.obsidian + currentState.obsidianRobots,
                geodesCracked = currentState.geodesCracked + currentState.geodeRobots,))
            miningStates.add(currentState.copy(minute = currentState.minute + 1,
                oreRobots = currentState.oreRobots + 1,
                ore = currentState.ore + currentState.oreRobots - blueprint.oreRobotOreCost,
                clay = currentState.clay + currentState.clayRobots,
                obsidian = currentState.obsidian + currentState.obsidianRobots,
                geodesCracked = currentState.geodesCracked + currentState.geodeRobots,))
        } else if ((currentState.ore >= blueprint.clayRobotOreCost && currentState.clayRobots < blueprint.obsidianRobotClayCost) &&
            !(currentState.ore >= blueprint.oreRobotOreCost && currentState.oreRobots < maxOreCost)) {
            miningStates.add(currentState.copy(minute = currentState.minute + 1,
                clayRobots = currentState.clayRobots + 1,
                ore = currentState.ore + currentState.oreRobots - blueprint.clayRobotOreCost,
                clay = currentState.clay + currentState.clayRobots,
                obsidian = currentState.obsidian + currentState.obsidianRobots,
                geodesCracked = currentState.geodesCracked + currentState.geodeRobots,))
            miningStates.add(currentState.copy(minute = currentState.minute + 1,
                ore = currentState.ore + currentState.oreRobots,
                clay = currentState.clay + currentState.clayRobots,
                obsidian = currentState.obsidian + currentState.obsidianRobots,
                geodesCracked = currentState.geodesCracked + currentState.geodeRobots,))
        } else if (!(currentState.ore >= blueprint.clayRobotOreCost && currentState.clayRobots < blueprint.obsidianRobotClayCost) &&
            (currentState.ore >= blueprint.oreRobotOreCost && currentState.oreRobots < maxOreCost)) {
            miningStates.add(currentState.copy(minute = currentState.minute + 1,
                oreRobots = currentState.oreRobots + 1,
                ore = currentState.ore + currentState.oreRobots - blueprint.oreRobotOreCost,
                clay = currentState.clay + currentState.clayRobots,
                obsidian = currentState.obsidian + currentState.obsidianRobots,
                geodesCracked = currentState.geodesCracked + currentState.geodeRobots,))
            miningStates.add(currentState.copy(minute = currentState.minute + 1,
                ore = currentState.ore + currentState.oreRobots,
                clay = currentState.clay + currentState.clayRobots,
                obsidian = currentState.obsidian + currentState.obsidianRobots,
                geodesCracked = currentState.geodesCracked + currentState.geodeRobots,))
        } else {
            miningStates.add(currentState.copy(minute = currentState.minute + 1,
                ore = currentState.ore + currentState.oreRobots,
                clay = currentState.clay + currentState.clayRobots,
                obsidian = currentState.obsidian + currentState.obsidianRobots,
                geodesCracked = currentState.geodesCracked + currentState.geodeRobots,))
        }
    }

    return maxProduced
}

private data class MiningState(var minute: Int = 0, var oreRobots: Int = 1, var clayRobots: Int = 0, var obsidianRobots: Int = 0,
                               var geodeRobots: Int = 0, var ore: Int = 0, var clay: Int = 0, var obsidian: Int = 0, var geodesCracked: Int = 0)

private fun parseBlueprints(input: List<String>): List<Blueprint> {
    return input.map(fun (line): Blueprint {
        val mainParts = line.split(": ")
        val costParts = mainParts[1].split(". ")
        return Blueprint(number = mainParts[0].split(" ")[1].toInt(),
            oreRobotOreCost = costParts[0].split(" ")[4].toInt(),
            clayRobotOreCost = costParts[1].split(" ")[4].toInt(),
            obsidianRobotOreCost = costParts[2].split(" ")[4].toInt(),
            obsidianRobotClayCost = costParts[2].split(" ")[7].toInt(),
            geodeRobotOreCost = costParts[3].split(" ")[4].toInt(),
            geodeRobotObsidianCost = costParts[3].split(" ")[7].toInt())
    })
}

private data class Blueprint(val number: Int,
                             val oreRobotOreCost: Int,
                             val clayRobotOreCost: Int,
                             val obsidianRobotOreCost: Int, val obsidianRobotClayCost: Int,
                             val geodeRobotOreCost: Int, val geodeRobotObsidianCost: Int)

fun day19Puzzle02(): String {
    val blueprints = parseBlueprints(readDayInput(19)).subList(0,3)
    return blueprints.fold(1) { acc, b -> acc * getBlueprintMaxProduced(b, 32) }.toString()
}