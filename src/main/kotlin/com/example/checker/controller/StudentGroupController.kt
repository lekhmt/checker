package com.example.checker.controller

import com.example.checker.entity.dto.StudentGroupInput
import com.example.checker.service.StudentGroupService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class StudentGroupController(
    private val studentGroupService: StudentGroupService,
) {

    @GetMapping("/groups")
    fun getAllGroups() = studentGroupService.getAllGroups()

    @PostMapping("/groups/save")
    fun saveGroup(@RequestBody studentGroup: StudentGroupInput) = studentGroupService.saveGroup(studentGroup)

//    @PutMapping("/groups/{groupName}/add-student/{studentEmail}")
//    fun addStudentInGroup(
//        @PathVariable groupName: String,
//        @PathVariable studentEmail: String,
//    ) = studentGroupService.addStudentInGroup(groupName, studentEmail)

}