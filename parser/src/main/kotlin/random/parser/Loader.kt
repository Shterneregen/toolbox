package random.parser

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class Loader(private val personRepository: PersonRepository) : CommandLineRunner {
    private val log = logger()

    @Value("\${file.to.parse}")
    private lateinit var fileToParse: String

    override fun run(vararg args: String?) {
        val count = personRepository.count()
        if (count > 0) {
            log.info("DB is ready. Size: {}", count)
            return
        }

        val file = File(fileToParse)
        if (file.exists()) {
            val persons = mutableListOf<Person>()
            file.forEachLine { persons.add(parsePerson(it)) }
            personRepository.saveAll(persons)
        } else {
            log.info("File '$fileToParse' not found")
        }

        log.info("Records: {}", personRepository.count())
    }

    fun parsePerson(text: String): Person {
        return "(.*)\t(\\d{1,2}\\.\\d{1,2}\\.\\d{4})\t(.*)\t(\\d{8,20})\t(.*)".toRegex()
            .matchEntire(text)?.destructured?.let { (name, dayOfBirth, address, num, notes) ->
                Person(name, parseDate(dayOfBirth), address, num, notes)
            } ?: throw IllegalArgumentException("Bad input '$text'")
    }

    private fun parseDate(dayOfBirth: String): LocalDate =
        LocalDate.parse(dayOfBirth, DateTimeFormatter.ofPattern("d.M.yyyy"))
}
