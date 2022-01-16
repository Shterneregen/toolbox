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
        else -> coloredPrintln("Specify property file using -p flag", Colors.RED.code)
    }
}

fun createBackUps(propFile: String, createInNewFolder: Boolean) {
    if (!File(propFile).exists()) {
        coloredPrintln("$propFile doesn't exist!", Colors.RED.code)
        return
    }
    val prop = Properties()
    prop.load(FileInputStream(propFile))

    val folderForBkps = File(prop["folderForBkps"] as String).also { folder -> if (!folder.exists()) folder.mkdir() }
    val files = prop.getProperty("files").split(",")
    files.forEach { filePath -> createBkp(filePath, folderForBkps, createInNewFolder) }
}

fun createBkp(filePath: String, folderForBkps: File, createInNewFolder: Boolean) {
    val file = File(filePath)
    if (!file.exists()) {
        coloredPrintln("$filePath doesn't exist!", Colors.RED.code)
        return
    }

    val target = if (createInNewFolder) File("${folderForBkps.name}/${getCurrentDay()}/${file.name}")
    else File("${folderForBkps.name}/${file.name}")

    when {
        target.exists() && getFileSize(file) == getFileSize(target) -> {
            coloredPrintln("$filePath has not been modified!", Colors.GREY.code)
        }
        else -> {
            file.copyTo(target, true)
            coloredPrintln("$filePath copied!", Colors.GREEN.code)
        }
    }
}

fun coloredPrintln(text: String, colorCode: String = Colors.GREY.code) = println(coloredText(text, colorCode))
fun coloredText(text: String, colorCode: String = Colors.GREY.code) = "$colorCode$text${Colors.RESET.code}"

fun fileAttributes(file: File): BasicFileAttributes =
    Files.readAttributes(file.toPath(), BasicFileAttributes::class.java)

fun getFileSize(file: File) = fileAttributes(file).size()

fun getCurrentDay(): String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))


enum class Colors(val code: String) {
    RED("\u001b[31m"), GREEN("\u001b[32m"), GREY("\u001b[37m"), RESET("\u001b[0m")
}