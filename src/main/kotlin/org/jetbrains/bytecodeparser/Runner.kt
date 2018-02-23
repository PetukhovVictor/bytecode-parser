package org.jetbrains.bytecodeparser

import org.jetbrains.bytecodeparser.io.DirectoryWalker
import org.jetbrains.bytecodeparser.parser.BytecodeParser

class Runner {
    companion object {
        fun run(jarsDirectory: String) {
            val parser = BytecodeParser()

            DirectoryWalker(jarsDirectory).run {
                parser.parse(it)
            }
        }
    }
}