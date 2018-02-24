package org.jetbrains.bytecodeparser.parser

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipException
import java.util.zip.ZipFile

class ClassFilesExtractor {
    private val buf = ByteArray(1024)
    private val classesDir = "classes"

    private fun copyFromJar(path: File, zipFile: ZipFile, zipEntry: ZipEntry) {
        File(path.parent).mkdirs()
        val fileOutputStream = FileOutputStream(path)
        val fileInputStream: InputStream = zipFile.getInputStream(zipEntry)
        var n: Int = fileInputStream.read(buf, 0, 1024)
        while (n > -1) {
            fileOutputStream.write(buf, 0, n)
            n = fileInputStream.read(buf, 0, 1024)
        }
        fileInputStream.close()
        fileOutputStream.close()
    }

    fun extract(file: File): MutableList<File>? {
        try {
            val zipArchive = ZipFile(file)
            val pathFolder = "${file.parent}/$classesDir"
            val classFiles: MutableList<File> = mutableListOf()

            zipArchive.use { zipFile ->
                val zipEntries = zipFile.entries()
                while (zipEntries.hasMoreElements()) {
                    val zipEntry = zipEntries.nextElement()
                    val path = "$pathFolder/${zipEntry.name}"
                    val currentFile = File(path)

                    if (currentFile.extension == "class") {
                        val classFile = File("$pathFolder/${zipEntry.name}")
                        copyFromJar(classFile, zipArchive, zipEntry)
                        classFiles.add(classFile)
                    }
                }
            }

            zipArchive.close()

            return classFiles
        } catch (e: ZipException) {
            println("ERROR: $e")
            return null
        }
    }
}