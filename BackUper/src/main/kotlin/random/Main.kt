package random

import random.FileUtils.createBackUps
import random.PrintUtils.errorPrintln

fun main(args: Array<String>) {
    val createInNewFolder = args.contains("-n")
    val addToZip = args.contains("-zip")

    when {
        (args.contains("-p")) -> {
            val propFile = args[args.indexOf("-p") + 1]
            createBackUps(propFile, createInNewFolder, addToZip)
        }

        else -> errorPrintln("Specify property file using -p flag")
    }
}
