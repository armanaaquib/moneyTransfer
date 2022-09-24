package com.bsf.moneyTransfer.exception.handler

import com.bsf.moneyTransfer.dto.ApiError
import com.bsf.moneyTransfer.dto.ApiResponse
import com.bsf.moneyTransfer.exception.InsufficientBalanceException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalDefaultExceptionHandler {

    @ExceptionHandler(InsufficientBalanceException::class)
    fun handleInsufficientBalanceException(exception: InsufficientBalanceException): ResponseEntity<ApiResponse<Error>> {
        return ResponseEntity(ApiResponse(error = ApiError(exception.message)), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<ApiResponse<Error>> {
        return ResponseEntity(
            ApiResponse(error = ApiError(exception.message ?: "Internal server error")),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

}