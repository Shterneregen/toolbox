package random.parser

import java.time.LocalDate
import javax.persistence.*

@Entity
data class Person(
    @Column(length = 50) val name: String? = null,
    val dayOfBirth: LocalDate? = null,
    @Column(length = 300) val address: String? = null,
    @Column(length = 20) val num: String? = null,
    @Column(length = 300) val note: String? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}
