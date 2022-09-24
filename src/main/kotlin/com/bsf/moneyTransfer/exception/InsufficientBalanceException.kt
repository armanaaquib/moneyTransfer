package com.bsf.moneyTransfer.exception

import java.math.BigDecimal

class InsufficientBalanceException(amount: BigDecimal) : Exception() {
    override val message: String = "Insufficient available balance to transfer $amount"
}
