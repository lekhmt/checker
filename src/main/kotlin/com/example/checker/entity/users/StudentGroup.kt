package com.example.checker.entity.users

import com.example.checker.entity.fragments.LongIdFragment
import com.example.checker.entity.tasks.Subject
import com.example.checker.entity.tasks.Task
import org.babyfish.jimmer.kt.ImmutableCompanion
import org.babyfish.jimmer.sql.*

@Entity
@Table(name = "student_groups")
interface StudentGroup : LongIdFragment {

    @Key
    val name: String

    val course: Int

    val institute: Int

    @OneToMany(mappedBy = "group")
    val students: List<Student>

    @ManyToMany(mappedBy = "groups")
    val curators: List<Curator>

    @ManyToMany(mappedBy = "groups")
    val subjects: List<Subject>

    companion object : ImmutableCompanion<StudentGroup>

}
