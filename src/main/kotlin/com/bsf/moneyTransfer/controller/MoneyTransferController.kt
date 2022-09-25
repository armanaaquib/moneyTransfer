package com.bsf.moneyTransfer.controller

import com.bsf.moneyTransfer.dto.ApiError
import com.bsf.moneyTransfer.dto.FailureResponse
import com.bsf.moneyTransfer.dto.MoneyTransferRequest
import com.bsf.moneyTransfer.exception.AccountNotFoundException
import com.bsf.moneyTransfer.service.MoneyTransferService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/moneyTransfers")
class MoneyTransferController(private val moneyTransferService: MoneyTransferService) {

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun transferMoney(@RequestBody moneyTransferRequest: MoneyTransferRequest) {
        moneyTransferService.transferMoney(
            moneyTransferRequest.senderAccountNumber,
            moneyTransferRequest.receiverAccountNumber,
            moneyTransferRequest.amount
        )
    }

    @ExceptionHandler(AccountNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleAccountNotFoundException(exception: AccountNotFoundException): FailureResponse {
        return FailureResponse(error = ApiError(exception.message))
    }

}
