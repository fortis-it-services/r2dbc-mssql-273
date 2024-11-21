package de.fortisit.r2dbcmssql273

import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import reactor.core.publisher.Flux

@SpringBootApplication
class R2dbcMssql273Application {

    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
        val initializer = ConnectionFactoryInitializer()
        initializer.setConnectionFactory(connectionFactory)
        initializer.setDatabasePopulator(
            ResourceDatabasePopulator(
                ClassPathResource("schema.sql")
            )
        )
        return initializer
    }

    @Bean
    fun runner(
        fooRepository: FooRepository,
        barRepository: BarRepository,
    ): ApplicationRunner = ApplicationRunner {
        Flux.merge(
            Flux.range(1, 1000)
                .map { Foo(name = it.toString()) }
                .flatMap { fooRepository.save(it) }
                .flatMap { fooRepository.findByName(it.name) },

            Flux.range(1, 1000)
                .map { Bar(someTitle = it.toString(), someOtherThing = it.toLong()) }
                .flatMap { barRepository.save(it) }
                .flatMap {
                    Flux.merge(
                        barRepository.findBySomeTitle(it.someTitle),
                        barRepository.findBySomeOtherThing(it.someOtherThing),
                    )
                }
        )
            .then(barRepository.count())
            .doOnNext { println(it) }
            .block()
    }

}

fun main(args: Array<String>) {
    runApplication<R2dbcMssql273Application>(*args)
}

@Table
data class Foo(
    val name: String,
    @Id val id: Long = 0,
)

interface FooRepository : ReactiveCrudRepository<Foo, Long> {
    fun findByName(name: String): Flux<Foo>
}

@Table
data class Bar(
    val someTitle: String,
    val someOtherThing: Long,
    @Id val id: Long = 0,
)

interface BarRepository : ReactiveCrudRepository<Bar, Long> {
    fun findBySomeTitle(someTitle: String): Flux<Bar>
    fun findBySomeOtherThing(someOtherThing: Long): Flux<Bar>
}
