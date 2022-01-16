package random

import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Properties

fun main(args: Array<String>) {
    val createInNewFolder = args.contains("-n")

    when {
        (args.contains("-p")) -> {
            val propFile = args[1 + args.indexOf("-p")]
            createBackUps(propFile, createInNewFolder)
        }
        else -> coloredPrintln("Specify property file using -p flag", Color.RED)
    }
}

fun createBackUps(propFile: String, createInNewFolder: Boolean) {
    if (!File(propFile).exists()) {
        coloredPrintln("$propFile doesn't exist!", Color.RED)
        return
    }
    val prop = Properties()
    prop.load(FileInputStream(propFile))

    val folderForBkps = File(prop["folderForBkps"] as String)
    println("Backup folder: ${folderForBkps.path}")
    prop.getProperty("files")?.split(",")
        ?.onEach { filePath -> createBkp(filePath, folderForBkps, createInNewFolder) }
        ?: run { coloredPrintln("No files to back up found", Color.RED) }
}

fun createBkp(filePath: String, folderForBkps: File, createInNewFolder: Boolean) {
    val file = File(filePath)
    if (!file.exists()) {
        coloredPrintln("$filePath doesn't exist!", Color.RED)
        return
    }

    val target = if (createInNewFolder) File("${folderForBkps.path}/${getCurrentDay()}/${file.name}")
    else File("${folderForBkps.path}/${file.name}")

    when {
        target.exists() && getFileSize(file) == getFileSize(target) -> {
            coloredPrintln("${file.path} has not been modified!", Color.GREY)
        }
        else -> {
            file.copyTo(target, true)
            println("${file.path} copied!\t-->\t${coloredText(target.path, Color.GREEN)}")
        }
    }
}

fun coloredPrintln(text: String, color: Color = Color.GREY) = println(coloredText(text, color))
fun coloredText(text: String, color: Color = Color.GREY) = "${color.code}$text${Color.RESET.code}"

fun fileAttributes(file: File): BasicFileAttributes =
    Files.readAttributes(file.toPath(), BasicFileAttributes::class.java)

fun getFileSize(file: File) = fileAttributes(file).size()

fun getCurrentDay(): String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))


enum class Color(val code: String) {
    RED("\u001b[31m"), GREEN("\u001b[32m"), GREY("\u001b[37m"), RESET("\u001b[0m")
}