package random

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Utils {
    fun getCurrentDayStr(): String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}
