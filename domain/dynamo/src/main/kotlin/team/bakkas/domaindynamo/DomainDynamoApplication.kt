package team.bakkas.domaindynamo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.AbstractEnvironment

@SpringBootApplication(scanBasePackages = ["team.bakkas"])
class DomainDynamoApplication

fun main(args: Array<String>) {

    runApplication<DomainDynamoApplication>(*args)
}
