package com.bsf.moneyTransfer.controller

import com.bsf.moneyTransfer.dto.ApiError
import com.bsf.moneyTransfer.dto.ApiResponse
import com.bsf.moneyTransfer.dto.TransferRequest
import com.bsf.moneyTransfer.exception.AccountNotFoundException
import com.bsf.moneyTransfer.service.TransferService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/transfers")
class TransferController(private val transferService: TransferService) {

    @PostMapping
    fun transferMoney(@RequestBody transferRequest: TransferRequest): ResponseEntity<Unit> {
        transferService.transferMoney(
            transferRequest.senderAccountNumber,
            transferRequest.receiverAccountNumber,
            transferRequest.amount
        )
        return ResponseEntity.noContent().build()
    }

    @ExceptionHandler(AccountNotFoundException::class)
    fun handleAccountNotFoundException(exception: AccountNotFoundException): ResponseEntity<ApiResponse<Error>> {
        return ResponseEntity.badRequest().body(ApiResponse(error = ApiError(exception.message)))
    }

}
