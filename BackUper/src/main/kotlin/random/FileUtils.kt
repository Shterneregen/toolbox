package random

import random.PrintUtils.coloredPrintln
import random.PrintUtils.errorPrintln
import random.PrintUtils.successPrintln
import random.Utils.getCurrentDayStr
import java.io.*
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object FileUtils {
    private val currentDay = getCurrentDayStr()

    fun createBackUps(propFile: String, createInNewFolder: Boolean, addToZip: Boolean) {
        if (!File(propFile).exists()) {
            errorPrintln("$propFile doesn't exist!")
            return
        }
        val prop = Properties().apply { load(FileInputStream(propFile)) }

        val folderForBkps = File(prop["folderForBkps"] as String)
        println("Backup folder: ${folderForBkps.path}")

        val files: List<String> = prop.getProperty("files")?.split(",")?.map { it.trim() }
            ?: run {
                errorPrintln("No files to back up found")
                return
            }

        if (addToZip) {
            val target = File("${folderForBkps.path}/$currentDay.zip")
            if (target.exists()) run {
                errorPrintln("Backup ZIP already created for today")
                return
            }

            addFilesToZip(files, target)
            return
        }

        files.onEach { filePath -> createBkp(filePath, folderForBkps, createInNewFolder) }
    }

    private fun createBkp(filePath: String, folderForBkps: File, createInNewFolder: Boolean) {
        val file = File(filePath)
        if (!file.exists()) {
            errorPrintln("$filePath doesn't exist!")
            return
        }

        val target = File("${folderForBkps.path}/" + (if (createInNewFolder) "$currentDay/" else "") + file.name)

        when {
            target.exists() && getFileSize(file) == getFileSize(target) -> {
                coloredPrintln("${file.path} has not been modified!")
            }

            else -> {
                file.copyTo(target, true)
                println("${file.path} copied!\t-->\t${successPrintln(target.path)}")
            }
        }
    }

    private fun addFilesToZip(files: List<String>, targetZipPath: File) {
        ZipOutputStream(BufferedOutputStream(FileOutputStream(targetZipPath))).use { out ->
            for (file in files) {
                FileInputStream(file).use { fi ->
                    BufferedInputStream(fi).use { origin ->
                        val entry = ZipEntry(file.substring(file.lastIndexOf("/")))
                        out.putNextEntry(entry)
                        origin.copyTo(out, 1024)
                    }
                }
            }
        }
    }

    private fun getFileSize(file: File) = fileAttributes(file).size()

    private fun fileAttributes(file: File): BasicFileAttributes =
        Files.readAttributes(file.toPath(), BasicFileAttributes::class.java)
}
