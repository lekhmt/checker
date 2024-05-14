package com.example.checker.datainit

import com.example.checker.entity.dto.*
import com.example.checker.entity.tasks.Compiler
import com.example.checker.entity.tasks.Compiler.*
import com.example.checker.entity.tasks.SubmitStatus
import com.example.checker.entity.tasks.SubmitStatus.*
import com.example.checker.entity.users.Role
import com.example.checker.service.StudentGroupService
import com.example.checker.service.TasksService
import com.example.checker.service.UserService
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.time.temporal.ChronoUnit.DAYS
import java.time.temporal.ChronoUnit.WEEKS

@Component
@Transactional
class DataInit(
    private val userService: UserService,
    private val studentGroupService: StudentGroupService,
    private val tasksService: TasksService,
    private val sql: KSqlClient,
) : CommandLineRunner {

    override fun run(vararg args: String) {
        createGroup("М8О-406Б-20", 4, 8)
        createGroup("М8О-408Б-20", 4, 8)
        createGroup("М3О-301Б-21", 3, 3)
        createGroup("М8О-305Б-21", 4, 8)
        createGroup("М6О-106Б-23", 1, 6)

        createStudent("Иванов Иван Иванович", "ivanov@mai.ru", "ivanov", "М8О-406Б-20")
        createStudent("Степанов Степан Степанович", "stepanov@mai.ru", "stepanov", "М8О-408Б-20")

        createCurator(
            "Куратор 1", "curator@mai.ru", "curator", listOf(
                "М8О-406Б-20", "М8О-408Б-20", "М3О-301Б-21", "М8О-305Б-21", "М6О-106Б-23"
            )
        )

        val subjectId = createSubject(
            "ООП", "Курс объектно-ориентированного программирования",
            listOf("М8О-406Б-20", "М8О-408Б-20"),
            listOf("curator@mai.ru")
        )
        createSubject(
            "Базы данных", "Курс баз данных для 8 института",
            listOf("М8О-406Б-20", "М8О-408Б-20"),
            listOf("curator@mai.ru")
        )

        createTask(
            displayName = "ЛР 1",
            description = "Лабораторная работа 1",
            visibleFrom = Instant.now().minus(7, DAYS),
            subjectId = subjectId,
            dueTo = Instant.now().plus(14, DAYS)
        )
        createTask(
            displayName = "ЛР 2",
            goal = "Изучение принципов полиморфизма",
            description = "Необходимо реализовать тырыпыры необходимо реализовать тырыпыры необходимо реализовать тырыпыры" +
                    "необходимо реализовать тырыпыры необходимо реализовать тырыпыры необходимо реализовать тырыпыры необходимо реализовать тырыпыры" +
                    "необходимо реализовать тырыпыры необходимо реализовать тырыпыры необходимо реализовать тырыпыры необходимо реализовать тырыпыры" +
                    "необходимо реализовать тырыпыры необходимо реализовать тырыпыры необходимо реализовать тырыпыры необходимо реализовать тырыпыры",
            inputDescription = "Какое-то описание ввода",
            outputDescription = "Какое-то описание вывода",
            visibleFrom = Instant.now(),
            subjectId = subjectId,
            timeLimit = 2,
            memoryLimit = 128,
            dueTo = Instant.now().plus(14, DAYS)
        )
        createTask(
            displayName = "Просроченная работа",
            goal = "Вы не сдали",
            description = "Необходимо было реализовать тырыпыры необходимо реализовать тырыпыры необходимо реализовать тырыпыры" +
                    "необходимо реализовать тырыпыры необходимо реализовать тырыпыры необходимо реализовать тырыпыры необходимо реализовать тырыпыры" +
                    "необходимо реализовать тырыпыры необходимо реализовать тырыпыры необходимо реализовать тырыпыры необходимо реализовать тырыпыры" +
                    "необходимо реализовать тырыпыры необходимо реализовать тырыпыры необходимо реализовать тырыпыры необходимо реализовать тырыпыры",
            inputDescription = "Какое-то описание ввода",
            outputDescription = "Какое-то описание вывода",
            visibleFrom = Instant.now().minus(21, DAYS),
            subjectId = subjectId,
            timeLimit = 2,
            memoryLimit = 128,
            dueTo = Instant.now().minus(7, DAYS)
        )

        createSubmit(
            1,
            "ivanov@mai.ru",
            Instant.now().minus(6, DAYS),
            File("src/main/resources/files/empty-file.txt"),
            true,
            GPP_20,
            ACCEPTED
        )
        createSubmit(
            2,
            "ivanov@mai.ru",
            Instant.now().minus(1, DAYS),
            File("src/main/resources/files/empty-file.txt"),
            true,
            GPP_20,
            TIME_LIMIT
        )
        createSubmit(
            1,
            "stepanov@mai.ru",
            Instant.now().minus(8, DAYS),
            File("src/main/resources/files/empty-file.txt"),
            true,
            GPP_20,
            COMPILATION_ERROR
        )
        createSubmit(
            3,
            "stepanov@mai.ru",
            Instant.now().minus(12, DAYS),
            File("src/main/resources/files/empty-file.txt"),
            true,
            GPP_20,
            ACCEPTED
        )
    }

    private fun createGroup(
        name: String,
        course: Int,
        institute: Int,
    ) = studentGroupService.saveGroup(StudentGroupInput(name, course, institute))

    private fun createStudent(
        fullName: String,
        email: String,
        password: String,
        groupName: String,
    ) = userService.saveStudent(
        StudentInput(
            StudentInput.TargetOf_user(fullName, email, password, Role.STUDENT),
            StudentInput.TargetOf_group(groupName)
        )
    )

    private fun createCurator(
        fullName: String,
        email: String,
        password: String,
        groupNames: List<String>,
    ) = userService.saveCurator(
        CuratorInput(
            CuratorInput.TargetOf_user(fullName, email, password, Role.CURATOR),
            groupNames.map { CuratorInput.TargetOf_groups(it) }
        )
    )

    private fun createSubject(
        name: String,
        description: String,
        groups: List<String>,
        curators: List<String>,
    ) = tasksService.saveSubject(
        SubjectInput(
            name,
            description,
            groups.map { SubjectInput.TargetOf_groups(it) },
            curators.map { SubjectInput.TargetOf_curators.TargetOf_user(it) }.map { SubjectInput.TargetOf_curators(it) }
        )
    )

    private fun createTask(
        displayName: String,
        goal: String? = null,
        description: String,
        inputDescription: String? = null,
        outputDescription: String? = null,
        timeLimit: Long? = null,
        memoryLimit: Long? = null,
        visibleFrom: Instant,
        dueTo: Instant? = null,
        subjectId: Long,
    ) = tasksService.saveTask(
        TaskInput(
            displayName,
            goal,
            description,
            inputDescription,
            outputDescription,
            timeLimit,
            memoryLimit,
            visibleFrom,
            dueTo,
            TaskInput.TargetOf_subject(id = subjectId)
        )
    )

    private fun createSubmit(
        task: Long,
        student: String,
        submittedAt: Instant,
        code: File,
        isSingleFile: Boolean,
        compiler: Compiler,
        status: SubmitStatus,
    ) = tasksService.saveSubmit(
        SubmitInput(
            SubmitInput.TargetOf_task(task), SubmitInput.TargetOf_student(student),
            submittedAt, code, isSingleFile, compiler, status
        )
    )

}
