package com.example.checker.controller.portal

import com.example.checker.entity.users.Role
import com.example.checker.service.TasksService
import com.example.checker.service.UserService
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.time.Instant

@Controller
@RequestMapping("/portal")
class TasksController(
    private val tasksService: TasksService,
    private val userService: UserService,
) {

    @RequestMapping("/subjects/{id}")
    fun subjectPage(
        @PathVariable id: Long,
        authentication: Authentication,
        model: Model
    ): String {
        // TODO by user
        val fullName = userService.findUserByEmail(authentication.name).fullName
        val tasks = tasksService.getTaskBySubjectId(id)
        val subject = tasksService.getSubjectById(id)
        var submits = tasksService.getSubmitsBySubjectId(id).sortedByDescending { it.submittedAt }
        if (authentication.authorities.map { it.authority }.contains(Role.STUDENT.roleName)) {
            submits = submits.filter { it.student.email == authentication.name }
        }
        model.addAttribute("fullName", fullName)
        model.addAttribute("subjectName", subject.name)
        model.addAttribute("subjectId", subject.id)
        model.addAttribute("tasks", tasks)
        model.addAttribute("submits", submits)
        return "tasks/subject"
    }

    @RequestMapping("/subjects/{id}/{taskId}")
    fun taskPage(
        @PathVariable id: Long,
        @PathVariable taskId: Long,
        authentication: Authentication,
        model: Model
    ): String {
        val fullName = userService.findUserByEmail(authentication.name).fullName
        val subject = tasksService.getSubjectById(id)
        val task = tasksService.getTaskById(taskId)
        var submits = tasksService.getSubmitsBySubjectId(id).sortedByDescending { it.submittedAt }.filter { it.task.id == taskId }
        if (authentication.authorities.map { it.authority }.contains(Role.STUDENT.roleName)) {
            submits = submits.filter { it.student.email == authentication.name }
        }
        model.addAttribute("fullName", fullName)
        model.addAttribute("subject", subject)
        model.addAttribute("task", task)
        model.addAttribute("submits", submits)
        return if (task.dueTo != null && task.dueTo.isBefore(Instant.now())) "tasks/task-archive" else "tasks/task"
    }

}