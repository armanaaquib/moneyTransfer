package com.bsf.moneyTransfer.controller

import com.bsf.moneyTransfer.dto.ApiError
import com.bsf.moneyTransfer.dto.FailureResponse
import com.bsf.moneyTransfer.dto.SuccessResponse
import com.bsf.moneyTransfer.dto.TransactionRequest
import com.bsf.moneyTransfer.exception.AccountNotFoundException
import com.bsf.moneyTransfer.service.TransactionService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/transactions")
class TransactionController(private val transactionService: TransactionService) {

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun transferMoney(@Valid @RequestBody transactionRequest: TransactionRequest) {
        transactionService.transferMoney(
            transactionRequest.senderAccountNumber,
            transactionRequest.receiverAccountNumber,
            transactionRequest.amount
        )
    }

    @GetMapping("/{accountNumber}")
    fun getTransactions(@PathVariable accountNumber: String) =
        SuccessResponse(transactionService.getTransactions(accountNumber))

    @ExceptionHandler(AccountNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleAccountNotFoundException(exception: AccountNotFoundException): FailureResponse {
        return FailureResponse(error = ApiError(exception.message))
    }

}
