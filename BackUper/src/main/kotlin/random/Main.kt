package random

import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Properties

private val terminalSupportColoredOutput = isTerminalSupportColoredOutput()
private val currentDay = getCurrentDayStr()

fun main(args: Array<String>) {
    val createInNewFolder = args.contains("-n")

    when {
        (args.contains("-p")) -> {
            val propFile = args[1 + args.indexOf("-p")]
            createBackUps(propFile, createInNewFolder)
        }
        else -> errorPrintln("Specify property file using -p flag")
    }
}

private fun createBackUps(propFile: String, createInNewFolder: Boolean) {
    if (!File(propFile).exists()) {
        errorPrintln("$propFile doesn't exist!")
        return
    }
    val prop = Properties().apply { load(FileInputStream(propFile)) }

    val folderForBkps = File(prop["folderForBkps"] as String)
    println("Backup folder: ${folderForBkps.path}")
    prop.getProperty("files")?.split(",")
        ?.onEach { filePath -> createBkp(filePath, folderForBkps, createInNewFolder) }
        ?: run { errorPrintln("No files to back up found") }
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
            println("${file.path} copied!\t-->\t${coloredText(target.path, Color.GREEN)}")
        }
    }
}

private fun coloredPrintln(text: String, color: Color = Color.GREY) = println(coloredText(text, color))
private fun errorPrintln(text: String) = println(coloredText(text, Color.RED))
private fun coloredText(text: String, color: Color = Color.GREY): String =
    if (terminalSupportColoredOutput) "${color.code}$text${Color.RESET.code}" else text

private fun isTerminalSupportColoredOutput() = System.console() != null && System.getenv()["TERM"] != null

private fun fileAttributes(file: File): BasicFileAttributes =
    Files.readAttributes(file.toPath(), BasicFileAttributes::class.java)

private fun getFileSize(file: File) = fileAttributes(file).size()

private fun getCurrentDayStr(): String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))


enum class Color(val code: String) {
    RED("\u001b[31m"), GREEN("\u001b[32m"), GREY("\u001b[37m"), RESET("\u001b[0m")
}
