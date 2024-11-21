package de.fortisit.r2dbcmssql273

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class R2dbcMssql273ApplicationTests {

    @Test
    fun contextLoads() {
    }

}
