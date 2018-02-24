package org.jetbrains.bytecodeparser.grouper

import java.io.File

class BytecodeFilesGrouper {
    companion object {
        const val BYTECODE_JSON_EXT = "class.bc.json"
    }

    private val bytecodeFilenameRegex = Regex("(?:(?<package>.+?)\\.)?(?<class>[^.]+).$BYTECODE_JSON_EXT")

    fun group(file: File, username: String, repo: String) {
        val match = bytecodeFilenameRegex.matchEntire(file.name)

        if (match != null) {
            val packageName = if (match.groups["package"] != null) match.groups["package"]!!.value else ""
            val className = match.groups["class"]!!.value
            println("$packageName : $className ($username, $repo)")
        } else {
            println("MATCHING ERROR: $file")
        }
    }
}