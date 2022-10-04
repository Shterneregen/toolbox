import java.time.LocalDate
import java.util.stream.IntStream

// kotlinc -script time.kts

fun printDates(month: Month, year: Int) = IntStream.rangeClosed(1, month.daysInMonth).forEach() {
    val day = fillZeroForMonth(it)
    println("${year}/${month.number}/${day}")
}

enum class Month(val number: String, val daysInMonth: Int) {
    JANUARY("01", 31),
    FEBRUARY("02", 28),
    MARCH("03", 31),
    APRIL("04", 30),
    MAY("05", 31),
    JUNE("06", 30),
    JULY("07", 31),
    AUGUST("08", 31),
    SEPTEMBER("09", 30),
    OCTOBER("10", 31),
    NOVEMBER("11", 30),
    DECEMBER("12", 31)
}

if (args.isNotEmpty() && args.size >= 1) {
    println(args[0])
    val month = getMonth(args[0].toInt())
    val year: Int = if (args.size > 1) args[1].toInt() else LocalDate.now().year
    printDates(month, year)
} else {
    val now = LocalDate.now()
    val month = getMonth(now.month.value)
    val year = now.year
    printDates(month, year)
}
// printDates(Month.JULY, 2022)

fun getMonth(monthNum: Int): Month {
    val month = fillZeroForMonth(monthNum)
    return Month.values().find { it.number == month }
        ?: throw IllegalArgumentException("No month found for $month")
}

fun fillZeroForMonth(month: Int) = if (month < 10) "0${month}" else month.toString()
