package com.example.checker.entity.users

import com.example.checker.entity.fragments.LongIdFragment
import org.babyfish.jimmer.kt.ImmutableCompanion
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.ManyToOne
import org.babyfish.jimmer.sql.OneToOne
import org.babyfish.jimmer.sql.Table


@Entity
@Table(name = "students")
interface Student : LongIdFragment {

    @OneToOne
    val user: User

    @ManyToOne
    val group: StudentGroup

    companion object : ImmutableCompanion<Student>

}
