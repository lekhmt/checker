package com.example.checker.entity.tasks

import com.example.checker.entity.fragments.LongIdFragment
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.ManyToOne
import org.babyfish.jimmer.sql.Table
import java.io.File

@Entity
@Table(name = "tests")
interface Test : LongIdFragment {

    @ManyToOne
    val task: Task

    val input: ByteArray

    val output: ByteArray

}