package com.example.checker.service

import com.example.checker.configuration.findAll
import com.example.checker.entity.*
import com.example.checker.entity.dto.StudentGroupInput
import com.example.checker.entity.dto.StudentGroupView
import com.example.checker.entity.users.*
import org.babyfish.jimmer.kt.new
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.springframework.stereotype.Service

@Service
class StudentGroupService(
    private val sql: KSqlClient,
) {

    fun saveGroup(studentGroup: StudentGroupInput) = sql.save(StudentGroup {
        name = studentGroup.name
        course = studentGroup.course
        institute = studentGroup.institute
    }).modifiedEntity.id

    fun findByName(name: String) = sql.createQuery(StudentGroup::class) {
        where(table.name eq name)
        select(table.id)
    }.execute().single()

    fun getAllGroups() = sql.findAll(StudentGroupView::class)

    fun getGroupsByCurator(id: Long) = sql.createQuery(StudentGroup::class) {
        where(table.curators { this.id eq id })
        select(table.fetch(StudentGroupView::class))
    }.execute()

}