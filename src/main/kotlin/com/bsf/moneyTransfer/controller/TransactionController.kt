package com.bsf.moneyTransfer.controller

import com.bsf.moneyTransfer.dto.ApiError
import com.bsf.moneyTransfer.dto.FailureResponse
import com.bsf.moneyTransfer.dto.TransactionRequest
import com.bsf.moneyTransfer.exception.AccountNotFoundException
import com.bsf.moneyTransfer.service.TransactionService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/transactions")
class TransactionController(private val transactionService: TransactionService) {

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun transferMoney(@RequestBody transactionRequest: TransactionRequest) {
        transactionService.transferMoney(
            transactionRequest.senderAccountNumber,
            transactionRequest.receiverAccountNumber,
            transactionRequest.amount
        )
    }

    @ExceptionHandler(AccountNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleAccountNotFoundException(exception: AccountNotFoundException): FailureResponse {
        return FailureResponse(error = ApiError(exception.message))
    }

}
