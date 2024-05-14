package com.example.checker

import org.babyfish.jimmer.client.EnableImplicitApi
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableImplicitApi
class CheckerApplication

fun main(args: Array<String>) {
    runApplication<CheckerApplication>(*args)
}
