package com.example.checker.service

import com.example.checker.configuration.findAll
import com.example.checker.entity.dto.*
import com.example.checker.entity.tasks.*
import com.example.checker.entity.users.addBy
import org.babyfish.jimmer.kt.new
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.springframework.stereotype.Service

@Service
class TasksService(
    private val sql: KSqlClient,
    private val userService: UserService,
) {

    fun getAllSubjects() = sql.findAll(SubjectView::class)

    fun getAllTasks() = sql.findAll(TaskView::class)

    fun getAllSubmits() = sql.findAll(SubmitView::class)

    fun getSubjectById(id: Long) = sql.findById(SubjectView::class, id)!!

    fun getTaskById(id: Long) = sql.findById(TaskView::class, id)!!

    fun getSubmitsBySubjectId(id: Long) = sql.createQuery(Submit::class) {
        where(table.task.subject.id eq id)
        select(table.fetch(SubmitView::class))
    }.execute()

    fun getSubmitsByTaskId(id: Long) = sql.createQuery(Submit::class) {
        where(table.task.id eq id)
        select(table.fetch(SubmitView::class))
    }.execute()

    fun getTaskBySubjectId(id: Long) = sql.createQuery(Task::class) {
        where(table.subject.id eq id)
        select(table.fetch(TaskView::class))
    }.execute()

    fun saveSubject(subject: SubjectInput): Long {
        val groupIds = userService.getGroupIds(subject.groups?.map { it.name })
        val curatorIds = userService.getCuratorIds(subject.curators?.map { it.user.email })
        return sql.save(new(Subject::class).by {
            name = subject.name
            description = subject.description
            if (groupIds != null) {
                for (groupId in groupIds) {
                    groups().addBy { id = groupId }
                }
            }
            if (curatorIds != null) {
                for (curatorId in curatorIds) {
                    curators().addBy { id = curatorId }
                }
            }
        }).modifiedEntity.id
    }

    fun saveTask(task: TaskInput): Long {
        return sql.save(new(Task::class).by {
            displayName = task.displayName
            goal = task.goal
            description = task.description
            inputDescription = task.inputDescription
            outputDescription = task.outputDescription
            timeLimit = task.timeLimit
            memoryLimit = task.memoryLimit
            visibleFrom = task.visibleFrom
            dueTo = task.dueTo
            subject {
                id = task.subject.id!!
            }
        }).modifiedEntity.id
    }

    fun saveSubmit(submit: SubmitInput): Long {
        return sql.save(new(Submit::class).by {
            task {
                id = submit.task.id!!
            }
            student {
                email = submit.student.email
            }
            submittedAt = submit.submittedAt
            code = submit.code
            isSingleFile = submit.isSingleFile
            compiler = submit.compiler
            submitStatus = submit.submitStatus
        }).modifiedEntity.id
    }

}