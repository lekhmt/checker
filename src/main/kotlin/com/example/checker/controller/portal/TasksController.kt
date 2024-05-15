package com.example.checker.controller.portal

import com.example.checker.entity.dto.SubmitInput
import com.example.checker.entity.tasks.Compiler
import com.example.checker.entity.tasks.StudentCodeSubmit
import com.example.checker.entity.tasks.SubmitStatus
import com.example.checker.entity.users.Role
import com.example.checker.service.RunnerService
import com.example.checker.service.TasksService
import com.example.checker.service.UserService
import jakarta.validation.Valid
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.time.Instant

@Controller
@RequestMapping("/portal")
class TasksController(
    private val tasksService: TasksService,
    private val userService: UserService,
    private val runnerService: RunnerService,
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

    @GetMapping("/subjects/{id}/{taskId}")
    fun taskPage(
        @PathVariable id: Long,
        @PathVariable taskId: Long,
        authentication: Authentication,
        model: Model
    ): String {
        injectModelAttributes(model, id, taskId, authentication, StudentCodeSubmit())
        val task = tasksService.getTaskById(taskId)
        return if (task.dueTo != null && task.dueTo.isBefore(Instant.now())) "tasks/task-archive" else "tasks/task"
    }

    @PostMapping("/subjects/{id}/{taskId}")
    fun createSubmit(
        @PathVariable id: Long,
        @PathVariable taskId: Long,
        authentication: Authentication,
        @ModelAttribute studentCodeSubmit: StudentCodeSubmit,
        model: Model
    ): String {
        if (studentCodeSubmit.compiler == null) {
            model.addAttribute("compilerNotPicked", true)
            injectModelAttributes(model, id, taskId, authentication, studentCodeSubmit)
            return "tasks/task"
        } else if(studentCodeSubmit.code.isNullOrBlank()) {
            model.addAttribute("codeNotProvided", true)
            injectModelAttributes(model, id, taskId, authentication, studentCodeSubmit)
            return "tasks/task"
        } else {
            val submitId = tasksService.saveSubmit(
                SubmitInput(
                    task = SubmitInput.TargetOf_task(taskId),
                    student = SubmitInput.TargetOf_student(authentication.name),
                    submittedAt = Instant.now(),
                    code = studentCodeSubmit.code!!.toByteArray(),
                    isSingleFile = true,
                    compiler = studentCodeSubmit.compiler!!,
                    submitStatus = SubmitStatus.TESTING,
                )
            )
            runnerService.testSubmit(submitId)
            studentCodeSubmit.reset()
        }
        injectModelAttributes(model, id, taskId, authentication, studentCodeSubmit)
        return "redirect:/portal/subjects/$id/$taskId"
    }

    @GetMapping("/submits/{id}")
    fun getSubmitMessage(
        @PathVariable id: Long,
        authentication: Authentication,
        model: Model
    ): String {
        injectModelFullName(model, authentication)
        val submit = tasksService.getSubmitById(id)
        model.addAttribute("submit", submit)
        return "tasks/submit-msg"
    }

    private fun injectModelFullName(
        model: Model,
        authentication: Authentication
    ) {
        val fullName = userService.findUserByEmail(authentication.name).fullName
        model.addAttribute("fullName", fullName)
    }

    private fun injectModelAttributes(
        model: Model,
        id: Long,
        taskId: Long,
        authentication: Authentication,
        studentCodeSubmit: StudentCodeSubmit,
    ) {
        injectModelFullName(model, authentication)
        val subject = tasksService.getSubjectById(id)
        val task = tasksService.getTaskById(taskId)
        var submits =
            tasksService.getSubmitsBySubjectId(id).sortedByDescending { it.submittedAt }.filter { it.task.id == taskId }
        if (authentication.authorities.map { it.authority }.contains(Role.STUDENT.roleName)) {
            submits = submits.filter { it.student.email == authentication.name }
        }
        model.addAttribute("subject", subject)
        model.addAttribute("task", task)
        model.addAttribute("submits", submits)
        model.addAttribute("compilers", Compiler.entries)
        model.addAttribute("studentCodeSubmit", studentCodeSubmit)
    }

}