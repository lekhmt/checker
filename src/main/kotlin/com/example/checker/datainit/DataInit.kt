package com.example.checker.datainit

import com.example.checker.entity.dto.*
import com.example.checker.entity.tasks.Compiler
import com.example.checker.entity.tasks.Compiler.*
import com.example.checker.entity.tasks.SubmitStatus
import com.example.checker.entity.tasks.SubmitStatus.*
import com.example.checker.entity.users.Role
import com.example.checker.service.RunnerService
import com.example.checker.service.StudentGroupService
import com.example.checker.service.TasksService
import com.example.checker.service.UserService
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.time.Instant
import java.time.temporal.ChronoUnit.DAYS

@Component
@Transactional
class DataInit(
    private val userService: UserService,
    private val studentGroupService: StudentGroupService,
    private val tasksService: TasksService,
    private val runnerService: RunnerService,
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
        val da = createSubject(
            "Дискретный анализ", null,
            listOf("М8О-406Б-20", "М8О-408Б-20"),
            listOf("curator@mai.ru")
        )
        val pod = createSubject(
            "Параллельная обработка данных", null,
            listOf("М8О-406Б-20", "М8О-408Б-20"),
            listOf("curator@mai.ru")
        )
        val neuro = createSubject(
            "Нейроинформатика", "Курс по архитектурам нейронных сетей",
            listOf("М8О-406Б-20", "М8О-408Б-20"),
            listOf("curator@mai.ru")
        )

        createTask(
            displayName = "ЛР 1",
            goal = "Изучение основных операций.",
            description = "Даны два числа A и B. Необходимо вычислить их произведение A*B. Чтение из стандартного ввода, запись в стандартный вывод.",
            inputDescription = "Входные данные представляют собой два целых числа A и B, -10^9 < A, B < 10^9.",
            outputDescription = "Выведите одно единственное число - произведение A*B.",
            visibleFrom = Instant.now().minus(7, DAYS),
            subjectId = da,
            timeLimit = 2,
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
        val s = createTask(
            displayName = "Просроченная работа",
            goal = "Изучение основных операций",
            description = "Даны два числа A и B. Необходимо вычислить их произведение A*B. " +
                    "Чтение из стандартного ввода, запись в стандартный вывод",
            inputDescription = "Входные данные представляют собой два целых числа A и B, -10^9 < A, B < 10^9.",
            outputDescription = "Выведите одно единственное число - произведение A*B.",
            visibleFrom = Instant.now().minus(21, DAYS),
            subjectId = subjectId,
            timeLimit = 2,
            memoryLimit = 128,
            dueTo = Instant.now().minus(7, DAYS)
        )
        createTask(
            displayName = "Сортировки",
            description = "Лабораторная работа 1",
            visibleFrom = Instant.now().minus(5, DAYS),
            subjectId = da,
            dueTo = Instant.now().plus(18, DAYS)
        )
        createTask(
            displayName = "Деревья",
            description = "Лабораторная работа 2",
            visibleFrom = Instant.now(),
            subjectId = da,
            dueTo = Instant.now().plus(28, DAYS)
        )
        createTask(
            displayName = "Работа 1",
            description = "ЛР 1",
            visibleFrom = Instant.now().minus(14, DAYS),
            subjectId = pod,
        )
        createTask(
            displayName = "Работа по нейроинформатике",
            goal = "Работа с метриками IoU, MIoU.",
            description = "0. Гиперпараметры (модель, оптимизатор и его параметры, шедулер и имя функции ошибки) для экспериментов конфигурировать через класс-контейнер может быть `namedtuple`,`dataclass`, `pydantic.Model` или просто класс без методов (кроме __init__).\n" +
                    "1. Написать валидацию на каждой эпохе\n" +
                    "2. Написать расчет метрики IoU, (расчет должен быть векторным, выход тензор формы [BxC] где B - плоскость батча, С - класса)\n" +
                    "3. Написать расчет метрики MIoU на основе IoU. Также представить IoU в среднем по датасету на последней валидации в развертке по классам.\n" +
                    "(пользоваться уже написанным IoU)\n" +
                    "5. Обучить модель на приемлемое качество (хотя бы 25% MIoU на валидации). (Отбирать лучшее качество на валидации, отдельные запуски логировать в clearml табличку: номер пуска: MIoU в среднем по классам)\n" +
                    "4. Отобрать по 2 картинки на основе метрики IoU (усредненной по классам) из 3 квантилей по качеству: картинки, при ранжировании по качеству попадающие в первые 10%,  в промежутке между 15% - 25%, и 45% - 55%. (пользоваться уже написанным IoU)\n" +
                    "5. Отобрать лучшую по качеству IoU картинку по трем выбранным классам.\n" +
                    "6. Посчитать таблицу IoU в развертке по кадому классу для лучшей модели.\n" +
                    "* Таблицы, картинки, и ipynb-тетрадь собрать в архив. Картинки можно просто отобразить в тетради, уже без необходимости сохранения, как и таблицы. (Кроме таблицы с запусками, её приложить в архив. Таблицы, графики и т.д. Можно забрать из clearml).\n" +
                    "** При обучении можете попробовать другие ФО, или аугментации. Модель тоже можете поменять.",
            visibleFrom = Instant.now().minus(21, DAYS),
            subjectId = neuro,
            dueTo = Instant.now().minus(7, DAYS)
        )

        createSubmit(
            1,
            "ivanov@mai.ru",
            Instant.now().minus(6, DAYS),
            File("src/main/resources/files/empty-file.txt").readBytes(),
            true,
            GPP_20,
            ACCEPTED
        )
        createSubmit(
            2,
            "ivanov@mai.ru",
            Instant.now().minus(20, DAYS),
            File("src/main/resources/files/empty-file.txt").readBytes(),
            true,
            GPP_20,
            WRONG_ANSWER
        )
        createSubmit(
            2,
            "ivanov@mai.ru",
            Instant.now().minus(1, DAYS),
            File("src/main/resources/files/empty-file.txt").readBytes(),
            true,
            GPP_20,
            ACCEPTED
        )
        val submitId1 = createSubmit(
            1,
            "stepanov@mai.ru",
            Instant.now().minus(8, DAYS),
            File("src/main/resources/files/example.py").readBytes(),
            true,
            PYTHON3,
            ACCEPTED
        )
        createSubmit(
            3,
            "stepanov@mai.ru",
            Instant.now().minus(12, DAYS),
            File("src/main/resources/files/empty-file.txt").readBytes(),
            true,
            GPP_20,
            TIME_LIMIT
        )
        createSubmit(
            1,
            "stepanov@mai.ru",
            Instant.now().minus(12, DAYS),
            File("src/main/resources/files/example.cpp").readBytes(),
            true,
            GPP_20,
            RUNTIME_ERROR
        )
        val submitId2 = createSubmit(
            1,
            "stepanov@mai.ru",
            Instant.now().minus(12, DAYS),
            File("src/main/resources/files/example.java").readBytes(),
            true,
            JAVA_21,
            ACCEPTED
        )

        createTest(
            1,
            File("src/main/resources/files/in_1.txt").readBytes(),
            File("src/main/resources/files/out_1.txt").readBytes(),
        )
        createTest(
            1,
            File("src/main/resources/files/in_2.txt").readBytes(),
            File("src/main/resources/files/out_2.txt").readBytes(),
        )

//        runnerService.testSubmit(submitId1)
//        runnerService.testSubmit(submitId2)

        println()
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
        description: String?,
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
        code: ByteArray,
        isSingleFile: Boolean,
        compiler: Compiler,
        status: SubmitStatus,
    ) = tasksService.saveSubmit(
        SubmitInput(
            SubmitInput.TargetOf_task(task), SubmitInput.TargetOf_student(student),
            submittedAt, code, isSingleFile, compiler, status
        )
    )

    private fun createTest(
        task: Long,
        input: ByteArray,
        output: ByteArray,
    ) = tasksService.saveTest(TestInput(input, output, TestInput.TargetOf_task(task)))

}
