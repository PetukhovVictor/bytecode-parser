package org.jetbrains.bytecodeparser

import com.xenomachina.argparser.ArgParser

fun main(args : Array<String>) {
    val parser = ArgParser(args)
    val jarsDirectory by parser.storing("-i", "--input", help="path to folder with jar files")

    Runner.run(jarsDirectory)
}