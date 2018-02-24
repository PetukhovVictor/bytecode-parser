package org.jetbrains.bytecodeparser

import org.jetbrains.bytecodeparser.grouper.BytecodeFilesGrouper
import org.jetbrains.bytecodeparser.io.DirectoryWalker
import org.jetbrains.bytecodeparser.parser.BytecodeParser
import org.jetbrains.bytecodeparser.parser.ClassFilesExtractor
import java.io.File

enum class Stage {
    PARSING, GROUPING
}

class Runner {
    companion object {
        fun parse(jarsDirectory: String) {
            val parser = BytecodeParser()
            val extractor = ClassFilesExtractor()

            DirectoryWalker(jarsDirectory).run {
                if (it.isFile && it.extension == BytecodeParser.JAR_FILE_EXT) {
                    val classFilePaths = extractor.extract(it)

                    if (classFilePaths != null) {
                        classFilePaths.forEach { parser.parse(it) }
                        println("PARSED (${classFilePaths.size} CLASSES): ${it.absolutePath}")
                    }
                }
            }
        }

        fun group(classDirectory: String) {
            val grouper = BytecodeFilesGrouper()

            DirectoryWalker(classDirectory, maxDepth = 2).run {
                if (it.isDirectory) {
                    val repoIdentifier = it.relativeTo(File(classDirectory)).invariantSeparatorsPath.split("/")
                    if (repoIdentifier.size == 2) {
                        DirectoryWalker(it.absolutePath).run {
                            if (it.isFile && it.name.endsWith(BytecodeFilesGrouper.BYTECODE_JSON_EXT)) {
                                grouper.group(it, repoIdentifier[0], repoIdentifier[1])
                            }
                        }
                    }
                }
            }


        }

        fun run(stage: Stage, directory: String) {
           when (stage) {
               Stage.PARSING -> parse(directory)
               Stage.GROUPING -> group(directory)
           }
        }
    }
}