package org.jetbrains.bytecodeparser

import org.jetbrains.bytecodeparser.io.DirectoryWalker
import org.jetbrains.bytecodeparser.parser.BytecodeParser
import org.jetbrains.bytecodeparser.parser.ClassFilesExtractor

class Runner {
    companion object {
        fun run(jarsDirectory: String) {
            val parser = BytecodeParser()
            val extractor = ClassFilesExtractor()

            DirectoryWalker(jarsDirectory).run {
                if (it.isFile && it.extension == "jar") {
                    val classFilePaths = extractor.extract(it)
                    classFilePaths.forEach { parser.parse(it) }
                }
            }
        }
    }
}