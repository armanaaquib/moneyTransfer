package com.bsf.moneyTransfer.dto

import java.math.BigDecimal

data class MoneyTransferRequest(
    val senderAccountNumber: String,
    val receiverAccountNumber: String,
    val amount: BigDecimal
)
