package com.bsf.moneyTransfer.controller

import com.bsf.moneyTransfer.dto.ApiError
import com.bsf.moneyTransfer.dto.FailureResponse
import com.bsf.moneyTransfer.dto.SuccessResponse
import com.bsf.moneyTransfer.exception.AccountNotFoundException
import com.bsf.moneyTransfer.model.Account
import com.bsf.moneyTransfer.service.AccountService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/accounts")
class AccountController(private val accountService: AccountService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAccount(): ResponseEntity<SuccessResponse<Account>> {
        val account = accountService.createAccount()
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(account.accountNumber)
            .toUri();

        return ResponseEntity.created(location).body(SuccessResponse(account))
    }

    @GetMapping("/{accountNumber}")
    fun getAccount(@PathVariable accountNumber: String) = SuccessResponse(accountService.getAccount(accountNumber))

}
