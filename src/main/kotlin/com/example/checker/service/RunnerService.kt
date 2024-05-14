package com.example.checker.service

import com.example.checker.entity.dto.SubmitView
import com.example.checker.entity.tasks.Compiler.*
import org.apache.commons.io.IOUtils.contentEqualsIgnoreEOL
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream

@Service
class RunnerService(
    private val tasksService: TasksService,
    @Value("\${source}") private val sourceDirectory: String,
) {

    fun testSubmit(submitId: Long) {
        val submit = tasksService.getSubmitById(submitId)
        val tests = tasksService.getTestsByTaskId(submit.task.id)
        val numberOfTests = tests.size

        val taskSubmitDirectoryPath = prepareDirectory(submit)

        val submitFile = prepareFile(taskSubmitDirectoryPath, submit.compiler.submitFileName, submit.code)
        if (submit.compiler.compileCommands != null) {
            val compilationErrorFile = prepareFile(taskSubmitDirectoryPath, "compilation_error.txt")
            val compilationResult = runCommand(taskSubmitDirectoryPath, submit.compiler.compileCommands, error = compilationErrorFile)
            if (compilationResult != 0) {
                throw Exception("COMPILATION ERROR: ${compilationErrorFile.readText()}")
            }
        }

        for (i in 1..numberOfTests) {
            val inputFile = prepareFile("$taskSubmitDirectoryPath/../..", "in_$i.txt")
            val outputFile = prepareFile("$taskSubmitDirectoryPath/../..", "out_$i.txt")
            val resultFile = prepareFile(taskSubmitDirectoryPath, "$i.txt")
            val errorFile = prepareFile(taskSubmitDirectoryPath, "error_$i.txt")

            val resultCode = runCommand(
                taskSubmitDirectoryPath,
                submit.compiler.runCommands,
                inputFile,
                resultFile,
                errorFile,
            )
            if (resultCode != 0) {
                throw Exception("RUNTIME ERROR: ${errorFile.readText()}")
            }
            if (!contentEqualsIgnoreEOL(
                    FileInputStream(outputFile).reader(),
                    FileInputStream(resultFile).reader(),
            )) {
                throw Exception("EXPECTED:\n ${outputFile.readText()}\n BUT WAS:\n ${resultFile.readText()}")
            }
        }
    }

    private fun prepareDirectory(
        submit: SubmitView
    ): String {
        val taskDirectoryPath = "$sourceDirectory/${submit.task.id}"
        val taskDirectory = File(taskDirectoryPath)
        if (!taskDirectory.exists()) taskDirectory.mkdir()
        val testFiles = taskDirectory.listFiles()?.filter { it.isFile }
        if (testFiles.isNullOrEmpty()) {
            val tests = tasksService.getTestsByTaskId(submit.task.id)
            for (i in 1..tests.size) {
                prepareFile(taskDirectoryPath, "in_$i.txt", tests[i - 1].input)
                prepareFile(taskDirectoryPath, "out_$i.txt", tests[i - 1].output)
            }
        }
        val taskSubmitsDirectoryPath = "$taskDirectoryPath/submits/${submit.id}"
        val taskSubmitDirectory = File(taskSubmitsDirectoryPath)
        taskSubmitDirectory.mkdirs()

        return taskSubmitsDirectoryPath
    }

    private fun prepareFile(
        dir: String,
        name: String,
        content: ByteArray? = null
    ): File {
        val file = File("$dir/$name")
        file.createNewFile()
        if (content != null) file.writeBytes(content)
        return file
    }

    private fun runCommand(
        workingDirectory: String,
        command: List<String>,
        input: File? = null,
        output: File? = null,
        error: File? = null,
    ): Int {
        var process = ProcessBuilder(*command.toTypedArray()).directory(File(workingDirectory))
        if (input != null && output != null && error != null) {
            process = process
                .redirectInput(input)
                .redirectOutput(output)
        }
        if (error != null) {
            process = process.redirectError(error)
        }
        return process.start().waitFor()
    }

}