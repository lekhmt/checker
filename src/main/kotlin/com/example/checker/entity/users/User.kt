package com.example.checker.entity.users

import com.example.checker.entity.fragments.LongIdFragment
import com.example.checker.entity.tasks.Submit
import org.babyfish.jimmer.kt.ImmutableCompanion
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Key
import org.babyfish.jimmer.sql.OneToMany
import org.babyfish.jimmer.sql.Table

@Entity
@Table(name = "users")
interface User : LongIdFragment {

    val fullName: String

    @Key
    val email: String

    val password: String

    val role: Role

    @OneToMany(mappedBy = "student")
    val submits: List<Submit>

    companion object : ImmutableCompanion<User>

}
