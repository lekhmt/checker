package com.example.checker.controller.portal;

import com.example.checker.entity.users.Role
import com.example.checker.service.StudentGroupService
import com.example.checker.service.TasksService
import com.example.checker.service.UserService
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/portal")
class MainPageController(
    private val studentGroupService: StudentGroupService,
    private val userService: UserService,
    private val taskService: TasksService,
) {

    @RequestMapping("/curator")
    fun curatorMainPage(
        authentication: Authentication,
        model: Model
    ): String {
        // TODO by user
        val groups = studentGroupService.getAllGroups()
        val fullName = userService.findUserByEmail(authentication.name).fullName
        val subjects = taskService.getAllSubjects()
        model.addAttribute("fullName", fullName)
        model.addAttribute("groups", groups)
        model.addAttribute("subjects", subjects)
        return "curator/curator-main"
    }

    @RequestMapping("/student")
    fun studentMainPage(
        authentication: Authentication,
        model: Model
    ): String {
        // TODO by user
        val fullName = userService.findUserByEmail(authentication.name).fullName
        val tasks = taskService.getAllTasks()
        val subjects = taskService.getAllSubjects()
        model.addAttribute("fullName", fullName)
        model.addAttribute("tasks", tasks)
        model.addAttribute("subjects", subjects)
        return "student/student-main"
    }

}
