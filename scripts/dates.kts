import java.time.YearMonth
import java.util.stream.IntStream

// Show days of the current month:
//  kotlinc -script dates.kts

// Show days of a specific month:
//  kotlinc -script dates.kts -- -m

// Show days of a specific month of a specific year:
//  kotlinc -script dates.kts -- -m 6 -y 2026

val now = YearMonth.now()
val month = getArg("-m")?.let { it.toInt() } ?: now.monthValue
val year = getArg("-y")?.let { it.toInt() } ?: now.year

printDates(year, month)

fun printDates(year: Int, month: Int) {
    val yearMonth = YearMonth.of(year, month)
    val monthStr = fillPrefixZero(yearMonth.monthValue)
    IntStream.rangeClosed(1, yearMonth.lengthOfMonth()).forEach() {
        val day = fillPrefixZero(it)
        println("${yearMonth.year}/${monthStr}/${day}")
    }
}

fun getArg(arg: String): String? {
    return if (args.isNotEmpty() && args.contains(arg)) {
        if (args.size < args.indexOf(arg) + 2) {
            throw IllegalArgumentException("Missed '${arg}' parameter!")
        }
        args[args.indexOf(arg) + 1]
    } else null
}

fun fillPrefixZero(month: Int) = if (month < 10) "0${month}" else month.toString()
