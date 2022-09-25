package random.parser

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ParserApplication

fun main(args: Array<String>) {
    runApplication<ParserApplication>(*args)
}
