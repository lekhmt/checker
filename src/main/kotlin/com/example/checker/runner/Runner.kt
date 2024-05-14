package com.example.checker.runner

import com.example.checker.service.RunnerService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class Runner(
    val runnerService: RunnerService,
    @Value("\${source}") private val sourceDirectory: String,
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        val result = runnerService.runCommand("ls -l $sourceDirectory")
        println("****************************************\n ${result} \n ****************************************")
    }

}