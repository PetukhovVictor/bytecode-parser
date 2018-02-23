package org.jetbrains.bytecodeparser.parser

import java.io.File
import com.google.gson.Gson
import org.apache.bcel.classfile.ClassParser
import org.apache.bcel.generic.InstructionList

class BytecodeParser {
    companion object {
        const val JSON_FILE_EXT = "bc.json"
    }

    private fun write(file: File, instructionNames: MutableList<String>) {
        val classesDir = File("${file.parent}/classes")
        classesDir.mkdirs()
        val outputPath = File("$classesDir/${file.name}.$JSON_FILE_EXT")

        outputPath.writeText(Gson().toJson(instructionNames))
    }

    fun parse(file: File) {
        val classParsed = ClassParser(file.absolutePath).parse()
        val instructionNames = mutableListOf<String>()

        classParsed.methods.forEach {
            if (it.code == null) {
                println("\"$it\" method skip (no bytecode)")
                return@forEach
            }
            InstructionList(it.code.code).forEach { instructionNames.add(it.instruction.name) }
            println("\"$it\" method bytecode was written")
        }

        println("$file file was parsed")
        println("============================================")

        write(file, instructionNames)
    }
}