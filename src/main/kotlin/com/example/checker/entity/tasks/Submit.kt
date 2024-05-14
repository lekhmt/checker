package com.example.checker.entity.tasks

import com.example.checker.entity.fragments.LongIdFragment
import com.example.checker.entity.users.User
import org.babyfish.jimmer.kt.ImmutableCompanion
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.ManyToOne
import org.babyfish.jimmer.sql.Table
import java.io.File
import java.time.Instant

@Entity
@Table(name = "submits")
interface Submit : LongIdFragment {

    @ManyToOne
    val task: Task

    @ManyToOne
    val student: User

    val submittedAt: Instant

    val code: File

    val isSingleFile: Boolean

    val compiler: Compiler

    val submitStatus: SubmitStatus

    companion object : ImmutableCompanion<Submit>

}

enum class Compiler(val description: String) {
    GPP_14("GNU G++14"),
    GPP_17("GNU G++17"),
    GPP_20("GNU G++20"),
    JAVA_8("Java 8"),
    JAVA_21("Java 21"),
}

enum class SubmitStatus(val description: String) {
    NOT_DONE("Не выполнено"),
    TESTING("Тестируется"),
    ACCEPTED("Зачтено"),
    WRONG_ANSWER("Неправильный ответ"),
    TIME_LIMIT("Превышено ограничение по времени"),
    MEMORY_LIMIT("Превышено ограничение по памяти"),
    RUNTIME_ERROR("Ошибка времени выполнения"),
    COMPILATION_ERROR("Ошибка компиляции"),
}