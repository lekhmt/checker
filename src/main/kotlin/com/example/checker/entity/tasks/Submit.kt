package com.example.checker.entity.tasks

import com.example.checker.entity.fragments.LongIdFragment
import com.example.checker.entity.users.User
import jakarta.validation.constraints.NotEmpty
import org.babyfish.jimmer.kt.ImmutableCompanion
import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.ManyToOne
import org.babyfish.jimmer.sql.Table
import java.time.Instant

@Entity
@Table(name = "submits")
interface Submit : LongIdFragment {

    @ManyToOne
    val task: Task

    @ManyToOne
    val student: User

    val submittedAt: Instant

    @Column
    val code: ByteArray

    val isSingleFile: Boolean

    val compiler: Compiler

    val submitStatus: SubmitStatus

    val message: String?

    companion object : ImmutableCompanion<Submit>

}

enum class Compiler(
    val description: String,
    val compileCommands: List<String>? = null,
    val runCommands: List<String>,
    val submitFileName: String
) {
    GPP_14(
        description = "GNU G++14",
        compileCommands = listOf("gcc", "-std=c++14"),
        runCommands = listOf("./a.out"),
        submitFileName = "main.cpp",
    ),
    GPP_17(
        description = "GNU G++17",
        compileCommands = listOf("gcc", "-std=c++17"),
        runCommands = listOf("./a.out"),
        submitFileName = "main.cpp",
    ),
    GPP_20(
        description = "GNU G++20",
        compileCommands = listOf("gcc", "-std=c++20"),
        runCommands = listOf("./a.out"),
        submitFileName = "main.cpp",
    ),
    JAVA_8(
        description = "Java 8",
        compileCommands = listOf("javac", "-source", "8", "-target", "8", "Main.java"),
        runCommands = listOf("java", "-classpath", ".", "Main"),
        submitFileName = "Main.java"
    ),

    JAVA_21(
        description = "Java 21",
        compileCommands = listOf("javac", "-source", "21", "-target", "21", "Main.java"),
        runCommands = listOf("java", "-classpath", ".", "Main"),
        submitFileName = "Main.java"
    ),
    PYTHON3(
        description = "Python",
        runCommands = listOf("python3", "main.py"),
        submitFileName = "main.py"
    )

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

data class StudentCodeSubmit(
    var compiler: Compiler? = null,
    var code: String? = null,
) {
    fun reset() {
        this.compiler = null
        this.code = null
    }
}