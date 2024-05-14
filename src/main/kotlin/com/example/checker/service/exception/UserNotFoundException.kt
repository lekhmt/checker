package com.example.checker.service.exception

class UserNotFoundException(email: String) : Exception("Не найден пользователь $email")