package com.example.checker.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class RunnerService(
    @Value("\${source}") private val sourceDirectory: String,
) {

    fun runCommand(command: String): List<String> {
        val process = ProcessBuilder(command).start()
        process.waitFor()
        return process.inputReader().lines().toList()
    }

}