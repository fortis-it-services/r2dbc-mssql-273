package de.fortisit.r2dbcmssql273

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<R2dbcMssql273Application>().with(TestcontainersConfiguration::class).run(*args)
}
