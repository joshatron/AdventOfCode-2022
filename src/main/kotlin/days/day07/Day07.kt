package io.joshatron.aoc2022.days.day07

import io.joshatron.aoc2022.readDayInput

fun day07Puzzle01(): String {
    val root = parseFileStructure(readDayInput(7))

    return countSmallFolders(root).toString()
}

private fun countSmallFolders(root: FileSystemNode): Int {
    var total = 0
    if (root.size <= 100000) {
        total += root.size
    }
    for (child in root.children) {
        if (child.type == FileSystemNodeType.DIRECTORY) {
            total += countSmallFolders(child)
        }
    }

    return total
}

private fun printFileStructure(root: FileSystemNode) {
    printRemainingFileStructure(root, 0)
}

private fun printRemainingFileStructure(dir: FileSystemNode, level: Int) {
    println("${" ".repeat(level*2)}${dir.size} ${dir.name}")
    for (child in dir.children) {
        if (child.type == FileSystemNodeType.DIRECTORY) {
            printRemainingFileStructure(child, level + 1)
        } else {
            println("${" ".repeat((level+1)*2)}${child.size} ${child.name}")
        }
    }
}

private fun parseFileStructure(input: List<String>): FileSystemNode {
    val root = FileSystemNode(FileSystemNodeType.DIRECTORY, 0, "/", null, ArrayList())

    var i = 0
    var currentDir = root
    while (i < input.size) {
        val command = input[i]
        if (command.startsWith("$ cd")) {
            i++
            val newDir = command.substring(5)
            if (newDir == "/") {
                currentDir = root
            } else if (newDir == "..") {
                currentDir = if (currentDir.parent != null) currentDir.parent!! else root
            } else {
                var childFound = false
                for (child in currentDir.children) {
                    if (child.name == newDir) {
                        childFound = true
                        currentDir = child
                        break
                    }
                }
                if (!childFound) {
                    val newChild = FileSystemNode(FileSystemNodeType.DIRECTORY, 0, newDir, currentDir, ArrayList())
                    currentDir.children.add(newChild)
                    currentDir = newChild
                }
            }

        } else if (command == "$ ls") {
            i++
            while (i < input.size && input[i][0] != '$') {
                val fileDetails = input[i]
                val parts = fileDetails.split(" ")
                var childFound = false
                for (child in currentDir.children) {
                    if (child.name == parts[1]) {
                        childFound = true
                        break
                    }
                }
                if (!childFound) {
                    if (parts[0] == "dir") {
                        val newChild = FileSystemNode(FileSystemNodeType.DIRECTORY, 0, parts[1], currentDir, ArrayList())
                        currentDir.children.add(newChild)
                    } else {
                        val newChild = FileSystemNode(FileSystemNodeType.FILE, parts[0].toInt(), parts[1], currentDir, ArrayList())
                        currentDir.children.add(newChild)
                        currentDir.size += newChild.size
                        var tempParent = currentDir.parent
                        while (tempParent != null) {
                            tempParent.size += newChild.size
                            tempParent = tempParent.parent
                        }
                    }
                }
                i++
            }
        } else {
            println("Command '$command' not recognized.")
        }
    }

    return root
}

private class FileSystemNode(val type: FileSystemNodeType, var size: Int, val name: String, val parent: FileSystemNode?,
                             val children: MutableList<FileSystemNode>)

private enum class FileSystemNodeType {
    FILE,
    DIRECTORY
}

fun day07Puzzle02(): String {
    val root = parseFileStructure(readDayInput(7))
    val targetMinSizeToDelete = 30000000 - (70000000 - root.size)
    return findSmallestDirBiggerThanX(root, targetMinSizeToDelete).toString()
}

private fun findSmallestDirBiggerThanX(root: FileSystemNode, x: Int): Int {
    var min = 70000000
    if (root.size >= x) {
        min = root.size
    }
    for (child in root.children) {
        if (child.type == FileSystemNodeType.DIRECTORY) {
            val minInner = findSmallestDirBiggerThanX(child, x)
            if (minInner < min) {
                min = minInner
            }
        }
    }

    return min
}