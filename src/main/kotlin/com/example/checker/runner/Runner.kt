package com.example.checker.runner

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class runner : CommandLineRunner {

    override fun run(vararg args: String?) {
        val process = ProcessBuilder("g++", "--version").start()
        process.waitFor()
        val result = process.inputReader().lines().toList()
        println("****************************************\n ${result} \n ****************************************")
    }

}