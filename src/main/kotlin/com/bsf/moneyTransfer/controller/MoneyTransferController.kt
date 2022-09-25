package com.bsf.moneyTransfer.controller

import com.bsf.moneyTransfer.dto.ApiError
import com.bsf.moneyTransfer.dto.ApiResponse
import com.bsf.moneyTransfer.dto.MoneyTransferRequest
import com.bsf.moneyTransfer.exception.AccountNotFoundException
import com.bsf.moneyTransfer.service.MoneyTransferService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/moneyTransfers")
class MoneyTransferController(private val moneyTransferService: MoneyTransferService) {

    @PostMapping
    fun transferMoney(@RequestBody moneyTransferRequest: MoneyTransferRequest): ResponseEntity<Unit> {
        moneyTransferService.transferMoney(
            moneyTransferRequest.senderAccountNumber,
            moneyTransferRequest.receiverAccountNumber,
            moneyTransferRequest.amount
        )
        return ResponseEntity.noContent().build()
    }

    @ExceptionHandler(AccountNotFoundException::class)
    fun handleAccountNotFoundException(exception: AccountNotFoundException): ResponseEntity<ApiResponse<Error>> {
        return ResponseEntity.badRequest().body(ApiResponse(error = ApiError(exception.message)))
    }

}
