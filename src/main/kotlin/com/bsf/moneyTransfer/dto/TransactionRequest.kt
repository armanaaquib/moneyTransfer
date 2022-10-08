package com.bsf.moneyTransfer.dto

import com.bsf.moneyTransfer.model.Money
import javax.validation.Valid

data class TransactionRequest(
    val senderAccountNumber: String,
    val receiverAccountNumber: String,
    @field:Valid val amount: Money
)
