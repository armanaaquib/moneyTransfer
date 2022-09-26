package com.bsf.moneyTransfer.dto

import com.bsf.moneyTransfer.model.Money

data class TransactionRequest(
    val senderAccountNumber: String,
    val receiverAccountNumber: String,
    val amount: Money
)
