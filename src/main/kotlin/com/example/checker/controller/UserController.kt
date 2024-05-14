package com.example.checker.controller

import com.example.checker.entity.dto.CuratorInput
import com.example.checker.entity.dto.StudentInput
import com.example.checker.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
) {

    @GetMapping("/users")
    fun getUsers() = userService.findAllUsers()

    @GetMapping("/students")
    fun getStudents() = userService.findAllStudents()

    @PostMapping("/students/save")
    fun saveStudent(@RequestBody student: StudentInput) = userService.saveStudent(student)

    @GetMapping("/curators")
    fun getCurators() = userService.findAllCurators()

    @PostMapping("/curators/save")
    fun saveCurators(@RequestBody curator: CuratorInput) = userService.saveCurator(curator)

}