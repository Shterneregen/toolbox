package random

object PrintUtils {

    fun successPrintln(text: String) = println(coloredText(text, Color.GREEN))
    fun errorPrintln(text: String) = println(coloredText(text, Color.RED))

    fun coloredPrintln(text: String, color: Color = Color.GREY) = println(coloredText(text, color))

    private fun coloredText(text: String, color: Color = Color.GREY): String =
        if (isTerminalSupportColoredOutput()) "${color.code}$text${Color.RESET.code}" else text

    private fun isTerminalSupportColoredOutput() = System.console() != null && System.getenv()["TERM"] != null

    enum class Color(val code: String) {
        RED("\u001b[31m"), GREEN("\u001b[32m"), GREY("\u001b[37m"), RESET("\u001b[0m")
    }
}
