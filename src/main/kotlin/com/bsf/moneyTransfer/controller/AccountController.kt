package com.bsf.moneyTransfer.controller

import com.bsf.moneyTransfer.dto.ApiError
import com.bsf.moneyTransfer.dto.ApiResponse
import com.bsf.moneyTransfer.exception.AccountNotFoundException
import com.bsf.moneyTransfer.model.Account
import com.bsf.moneyTransfer.service.AccountService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/accounts")
class AccountController(private val accountService: AccountService) {

    @PostMapping
    fun createAccount(): ResponseEntity<ApiResponse<Account>> {
        val account = accountService.createAccount()
        return ResponseEntity(ApiResponse(account), HttpStatus.CREATED)
    }

    @GetMapping("/{accountNumber}")
    fun getAccount(@PathVariable accountNumber: String) = ApiResponse(accountService.getAccount(accountNumber))

    @ExceptionHandler(AccountNotFoundException::class)
    fun handleAccountNotFoundException(exception: AccountNotFoundException): ResponseEntity<ApiResponse<Error>> {
        return ResponseEntity(ApiResponse(error = ApiError(exception.message)), HttpStatus.NOT_FOUND)
    }
}
