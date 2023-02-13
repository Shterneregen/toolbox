import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoField
import java.util.stream.IntStream

// Show days of the current month:
//  kotlinc -script dates.kts

// Show days of a specific month:
//  kotlinc -script dates.kts -- -m 6

// Show days of a specific month of a specific year:
//  kotlinc -script dates.kts -- -m 6 -y 2026

// Use -w flag to show names of the days of the week
//  kotlinc -script dates.kts -- -w

val now = YearMonth.now()
val month = getStrArg("-m")?.let { it.toInt() } ?: now.monthValue
val year = getStrArg("-y")?.let { it.toInt() } ?: now.year
val showDaysOfWeek = checkBooleanFlag("-w")

printDates(year, month, showDaysOfWeek)

fun printDates(year: Int, month: Int, showDaysOfWeek: Boolean = false) {
    val yearMonth = YearMonth.of(year, month)
    val monthStr = fillPrefixZero(yearMonth.monthValue)
    IntStream.rangeClosed(1, yearMonth.lengthOfMonth()).forEach() {
        val day = fillPrefixZero(it)
        val atDay = yearMonth.atDay(it)

        val newLine = if (isStartOfWeek(atDay)) "\n" else ""
        val daysOfWeek = if (showDaysOfWeek) " ${atDay.dayOfWeek}" else ""
        println("${newLine}${yearMonth.year}/${monthStr}/${day}${daysOfWeek}")
    }
}

fun isStartOfWeek(day: LocalDate) = day.equals(day.with(ChronoField.DAY_OF_WEEK, 1))

fun getStrArg(arg: String): String? {
    return if (args.isNotEmpty() && args.contains(arg)) {
        if (args.size < args.indexOf(arg) + 2) {
            throw IllegalArgumentException("Missed '${arg}' parameter!")
        }
        args[args.indexOf(arg) + 1]
    } else null
}

fun checkBooleanFlag(arg: String) = args.isNotEmpty() && args.contains(arg)

fun fillPrefixZero(month: Int) = if (month < 10) "0${month}" else month.toString()
