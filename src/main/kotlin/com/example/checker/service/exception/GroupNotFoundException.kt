package com.example.checker.service.exception

class GroupNotFoundException(groupName: String) : Exception("Не найдена группа $groupName")