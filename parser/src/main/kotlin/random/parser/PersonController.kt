package random.parser

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/person")
class PersonController(private val personRepository: PersonRepository) {

    @GetMapping
    fun getPersons(
        @RequestParam name: String?,
        @RequestParam address: String?,
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 50
    ): Page<Person> {
        val pageable: Pageable = PageRequest.of(page, size)
        return personRepository.findByAddressLike(name, address, pageable)
    }
}
