package random.parser

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface PersonRepository : PagingAndSortingRepository<Person, Long> {

    @Query(
        """
        select p from Person p 
        where (:address is null or UPPER(p.address) like CONCAT('%',UPPER(:address),'%'))
            and (:name is null or UPPER(p.name) like CONCAT('%',UPPER(:name),'%'))
            and (:dayOfBirth is null or p.dayOfBirth = :dayOfBirth)
        """
    )
    fun findByAddressLike(
        @Param("name") name: String?,
        @Param("address") address: String?,
        @Param("dayOfBirth") dayOfBirth: LocalDate?,
        pageable: Pageable
    ): Page<Person>
}
