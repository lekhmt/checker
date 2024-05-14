package com.example.checker.controller.exceptionhandling

import com.example.checker.service.exception.GroupNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

data class ErrorMsg(
    val status: Int? = null,
    val message: String?,
)

@ControllerAdvice
class ExceptionControllerAdvice {

    @ExceptionHandler
    fun handleGroupNotFoundException(ex: GroupNotFoundException): ResponseEntity<ErrorMsg> {
        return ResponseEntity(ErrorMsg(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.message), HttpStatus.BAD_REQUEST)
    }

}