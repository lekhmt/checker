package com.example.checker.service

import com.example.checker.configuration.findAll
import com.example.checker.entity.dto.*
import com.example.checker.entity.tasks.*
import com.example.checker.entity.users.addBy
import com.example.checker.entity.users.id
import com.example.checker.entity.users.students
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.UnzipParameters
import org.babyfish.jimmer.kt.new
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.h2.util.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

@Service
class TasksService(
    private val sql: KSqlClient,
    private val userService: UserService,
    @Value("\${source}") private val sourceDirectory: String,
) {

    fun getAllSubjects() = sql.findAll(SubjectView::class)

    fun getAllTasks() = sql.findAll(TaskView::class)

    fun getAllSubmits() = sql.findAll(SubmitView::class)

    fun getSubjectById(id: Long) = sql.findById(SubjectView::class, id)!!

    fun getTaskById(id: Long) = sql.findById(TaskView::class, id)!!

    fun getSubmitById(id: Long) = sql.findById(SubmitView::class, id)!!

    fun getTestsByTaskId(id: Long) = sql.createQuery(Test::class) {
        where(table.task.id eq id)
        select(table.fetch(TestView::class))
    }.execute()

    fun getSubjectsByCurator(id: Long) = sql.createQuery(Subject::class) {
        where(table.curators { this.id eq id })
        select(table.fetch(SubjectView::class))
    }.execute()

    fun getSubjectsByStudent(id: Long) = sql.createQuery(Subject::class) {
        where(table.groups { this.students { this.id eq id } })
        select(table.fetch(SubjectView::class))
    }.execute()

    fun getSubmitsBySubjectId(id: Long) = sql.createQuery(Submit::class) {
        where(table.task.subject.id eq id)
        select(table.fetch(SubmitView::class))
    }.execute()

    fun getSubmitsByTaskId(id: Long) = sql.createQuery(Submit::class) {
        where(table.task.id eq id)
        select(table.fetch(SubmitView::class))
    }.execute()

    fun getTaskBySubjectId(id: Long) = sql.createQuery(Task::class) {
        where(table.subject.id eq id)
        select(table.fetch(TaskView::class))
    }.execute()

    fun getTasksForStudent(id: Long) = sql.createQuery(Task::class) {
        where(table.subject.groups { this.students { this.id eq id } })
        select(table.fetch(TaskView::class))
    }.execute()

    fun saveSubject(subject: SubjectInput): Long {
        val groupIds = userService.getGroupIds(subject.groups?.map { it.name })
        val curatorIds = userService.getCuratorIds(subject.curators?.map { it.user.email })
        return sql.save(new(Subject::class).by {
            name = subject.name
            description = subject.description
            if (groupIds != null) {
                for (groupId in groupIds) {
                    groups().addBy { id = groupId }
                }
            }
            if (curatorIds != null) {
                for (curatorId in curatorIds) {
                    curators().addBy { id = curatorId }
                }
            }
        }).modifiedEntity.id
    }

    fun saveTask(task: TaskInput): Long {
        return sql.save(new(Task::class).by {
            displayName = task.displayName
            goal = task.goal
            description = task.description
            inputDescription = task.inputDescription
            outputDescription = task.outputDescription
            timeLimit = task.timeLimit
            memoryLimit = task.memoryLimit
            visibleFrom = task.visibleFrom
            dueTo = task.dueTo
            subject {
                id = task.subject.id!!
            }
        }).modifiedEntity.id
    }

    fun saveSubmit(submit: SubmitInput): Long {
        return sql.save(new(Submit::class).by {
            task {
                id = submit.task.id!!
            }
            student {
                email = submit.student.email
            }
            submittedAt = submit.submittedAt
            code = submit.code
            isSingleFile = submit.isSingleFile
            compiler = submit.compiler
            submitStatus = submit.submitStatus
        }).modifiedEntity.id
    }

    fun saveTest(test: TestInput): Long {
        return sql.save(new(Test::class).by {
            task {
                id = test.task.id!!
            }
            input = test.input
            output = test.output
        }).modifiedEntity.id
    }

    fun updateSubmitStatus(
        id: Long,
        status: SubmitStatus,
        message: String? = null,
    ) = sql.update(Submit {
        this.id = id
        this.submitStatus = status
        this.message = message
    })

    fun saveTestsForTask(taskId: Long, tests: MultipartFile) {
        val taskDirectory = File("$sourceDirectory/$taskId")
        taskDirectory.mkdir()
        val tmpFile = File.createTempFile(UUID.randomUUID().toString(), "temp")
        val outputStream = FileOutputStream(tmpFile)
        IOUtils.copy(tests.inputStream, outputStream)
        outputStream.close()

        val zipFile = ZipFile(tmpFile)
        zipFile.extractAll(taskDirectory.path)

        var testFileExists = true
        var testFileNumber = 1
        while (testFileExists) {
            val inputFileName = "${taskDirectory.path}/in_$testFileNumber.txt"
            val inputFile = File(inputFileName)
            if (inputFile.exists()) {
                val outputFileName = "${taskDirectory.path}/out_$testFileNumber.txt"
                val outputFile = File(outputFileName)
                saveTest(TestInput(inputFile.readBytes(), outputFile.readBytes(), TestInput.TargetOf_task(taskId)))
                testFileNumber++
                continue
            }
            testFileExists = false
        }
    }

}