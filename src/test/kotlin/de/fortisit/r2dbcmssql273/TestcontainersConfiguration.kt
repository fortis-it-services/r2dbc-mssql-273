package de.fortisit.r2dbcmssql273

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.MSSQLServerContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    fun sqlServerContainer(): MSSQLServerContainer<*> {
        return MSSQLServerContainer(DockerImageName.parse("mcr.microsoft.com/mssql/server:latest"))
            .acceptLicense()
    }

}
