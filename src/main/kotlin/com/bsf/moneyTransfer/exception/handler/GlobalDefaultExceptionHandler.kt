package com.bsf.moneyTransfer.exception.handler

import com.bsf.moneyTransfer.dto.ApiError
import com.bsf.moneyTransfer.dto.FailureResponse
import com.bsf.moneyTransfer.exception.AccountNotFoundException
import com.bsf.moneyTransfer.exception.InsufficientBalanceException
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalDefaultExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AccountNotFoundException::class)
    fun handleAccountNotFoundException(exception: AccountNotFoundException): FailureResponse {
        return FailureResponse(ApiError(exception.message))
    }

    @ExceptionHandler(InsufficientBalanceException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInsufficientBalanceException(exception: InsufficientBalanceException): FailureResponse {
        return FailureResponse(ApiError(exception.message))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleHttpMessageNotReadableException(exception: HttpMessageNotReadableException): FailureResponse {
        return FailureResponse(ApiError(exception.message ?: "Invalid request"))
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(exception: Exception): FailureResponse {
        return FailureResponse(ApiError(exception.message ?: "Internal server error"))
    }

}
