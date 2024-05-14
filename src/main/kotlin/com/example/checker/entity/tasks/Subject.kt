package com.example.checker.entity.tasks

import com.example.checker.entity.fragments.LongIdFragment
import com.example.checker.entity.users.Curator
import com.example.checker.entity.users.StudentGroup
import org.babyfish.jimmer.kt.ImmutableCompanion
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Key
import org.babyfish.jimmer.sql.ManyToMany
import org.babyfish.jimmer.sql.OneToMany
import org.babyfish.jimmer.sql.Table

@Entity
@Table(name = "subjects")
interface Subject : LongIdFragment {

    @Key
    val name: String

    val description: String?

    @ManyToMany
    val groups: List<StudentGroup>

    @ManyToMany
    val curators: List<Curator>

    @OneToMany(mappedBy = "subject")
    val tasks: List<Task>

    companion object : ImmutableCompanion<Curator>

}