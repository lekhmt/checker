package com.example.checker.service

import com.example.checker.configuration.findAll
import com.example.checker.entity.dto.*
import com.example.checker.entity.users.*
import com.example.checker.service.exception.GroupNotFoundException
import com.example.checker.service.exception.UserNotFoundException
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val sql: KSqlClient,
    private val passwordEncoder: PasswordEncoder,
    private val studentGroupService: StudentGroupService,
) {

    fun saveStudent(student: StudentInput): Long {
        val groupId = try {
            studentGroupService.findByName(student.group.name)
        } catch (ex: Exception) {
            throw GroupNotFoundException(student.group.name)
        }
        return sql.save(Student {
            user {
                fullName = student.user.fullName
                email = student.user.email
                password = passwordEncoder.encode(student.user.password)
                role = student.user.role
            }
            group {
                id = groupId
            }
        }
        ).modifiedEntity.id
    }

    fun saveCurator(curator: CuratorInput): Long {
        val groupIds = getGroupIds(curator.groups?.map { it.name })
        return sql.save(Curator {
            user {
                fullName = curator.user.fullName
                email = curator.user.email
                password = passwordEncoder.encode(curator.user.password)
                role = curator.user.role
            }
            if (groupIds != null) {
                for (groupId in groupIds) {
                    groups().addBy { id = groupId }
                }
            }
        }).modifiedEntity.id
    }

    fun findUserByEmail(email: String): UserView {
        val user = sql.createQuery(User::class) {
            where(table.email eq email)
            select(table.fetch(UserView::class))
        }.execute().single()
        return user
    }

    fun findCuratorIdByEmail(email: String) = sql.createQuery(Curator::class) {
        where(table.user.email eq email)
        select(table.id)
    }.execute().single()

    fun findAllUsers() = sql.findAll(UserView::class)

    fun findAllStudents() = sql.findAll(StudenView::class)

    fun findAllCurators() = sql.findAll(CuratorView::class)

    fun getCuratorIds(emails: List<String>?): List<Long>? {
        return emails?.map {
            val id = try {
                findCuratorIdByEmail(it)
            } catch (ex: Exception) {
                throw UserNotFoundException(it)
            }
            id
        }
    }

    fun getGroupIds(names: List<String>?): List<Long>? {
        return names?.map {
            val id = try {
                studentGroupService.findByName(it)
            } catch (ex: Exception) {
                throw GroupNotFoundException(it)
            }
            id
        }
    }

}
