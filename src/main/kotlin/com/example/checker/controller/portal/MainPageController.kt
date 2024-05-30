package com.example.checker.controller.portal;

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
        val curator = userService.findCuratorByEmail(authentication.name)
        val groups = studentGroupService.getGroupsByCurator(curator.id)
        val subjects = taskService.getSubjectsByCurator(curator.id)
        model.addAttribute("fullName", curator.user.fullName)
        model.addAttribute("groups", groups)
        model.addAttribute("subjects", subjects)
        return "curator/curator-main"
    }

    @RequestMapping("/student")
    fun studentMainPage(
        authentication: Authentication,
        model: Model
    ): String {
        val student = userService.findStudentByEmail(authentication.name)
        val tasks = taskService.getTasksForStudent(student.id)
        val subjects = taskService.getSubjectsByStudent(student.id)
        model.addAttribute("fullName", student.user.fullName)
        model.addAttribute("tasks", tasks)
        model.addAttribute("subjects", subjects)
        return "student/student-main"
    }

}
