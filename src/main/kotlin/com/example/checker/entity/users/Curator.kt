package com.example.checker.entity.users

import com.example.checker.entity.fragments.LongIdFragment
import com.example.checker.entity.tasks.Subject
import org.babyfish.jimmer.kt.ImmutableCompanion
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.ManyToMany
import org.babyfish.jimmer.sql.OneToOne
import org.babyfish.jimmer.sql.Table

@Entity
@Table(name = "curators")
interface Curator : LongIdFragment {

    @OneToOne
    val user: User

    @ManyToMany
    val groups: List<StudentGroup>

    @ManyToMany(mappedBy = "curators")
    val subjects: List<Subject>

    companion object : ImmutableCompanion<Curator>

}