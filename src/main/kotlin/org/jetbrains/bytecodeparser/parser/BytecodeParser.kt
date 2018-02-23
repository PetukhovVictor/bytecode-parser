package org.jetbrains.bytecodeparser.parser

import java.io.File
import com.google.gson.Gson
import org.apache.bcel.classfile.ClassParser
import org.apache.bcel.generic.InstructionList
import java.nio.file.Files

typealias Instructions = MutableMap<String, MutableList<String>>

class BytecodeParser {
    companion object {
        const val JSON_FILE_EXT = "bc.json"
    }

    private fun write(file: File, packageName: String, instructions: Instructions) {
        if (Files.exists(File("${file.parent}/${file.name}.$JSON_FILE_EXT").toPath())) {
            Files.delete(File("${file.parent}/${file.name}.$JSON_FILE_EXT").toPath())
        }

        val outputPath = File("${file.parent}/$packageName:${file.name}.$JSON_FILE_EXT")

        outputPath.writeText(Gson().toJson(instructions))
    }

    fun parse(file: File) {
        val classParsed = ClassParser(file.absolutePath).parse()
        val instructions: Instructions = mutableMapOf()

        classParsed.methods.forEach {
            if (it.code == null) {
                // println("\"$it\" method skip (no bytecode)")
                return@forEach
            }
            val methodName = it.name
            instructions[methodName] = mutableListOf()
            InstructionList(it.code.code).forEach { instructions[methodName]!!.add(it.instruction.name) }
            // println("\"$it\" method bytecode was written")
        }

        println("PARSED: $file")
        write(file, classParsed.packageName, instructions)
    }
}