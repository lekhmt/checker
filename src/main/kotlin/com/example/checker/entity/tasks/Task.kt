package com.example.checker.entity.tasks

import com.example.checker.entity.fragments.LongIdFragment
import com.example.checker.entity.users.StudentGroup
import org.babyfish.jimmer.kt.ImmutableCompanion
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.ManyToMany
import org.babyfish.jimmer.sql.ManyToOne
import org.babyfish.jimmer.sql.OneToMany
import org.babyfish.jimmer.sql.Table
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.util.zip.ZipFile

@Entity
@Table(name = "tasks")
interface Task : LongIdFragment {

    val displayName: String

    val goal: String?

    val description: String

    val inputDescription: String?

    val outputDescription: String?

    val timeLimit: Long?

    val memoryLimit: Long?

    val visibleFrom: Instant

    val dueTo: Instant?

    @ManyToOne
    val subject: Subject

    @ManyToMany
    val visibleForGroups: List<StudentGroup>

    @OneToMany(mappedBy = "task")
    val tests: List<Test>

    @OneToMany(mappedBy = "task")
    val submits: List<Submit>

    companion object : ImmutableCompanion<Task>

}

data class CuratorTaskInfo(
    var displayName: String? = null,
    var goal: String? = null,
    var description: String? = null,
    var inputDescription: String? = null,
    var outputDescription: String? = null,
    var timeLimit: Long? = null,
    var memoryLimit: Long? = null,
    var visibleFrom: String? = null,
    var dueTo: String? = null,
    var tests: MultipartFile? = null,
) {

    fun clear() {
        if (goal.isNullOrEmpty()) goal = null
        if (inputDescription.isNullOrEmpty()) inputDescription = null
        if (outputDescription.isNullOrEmpty()) outputDescription = null
    }

}