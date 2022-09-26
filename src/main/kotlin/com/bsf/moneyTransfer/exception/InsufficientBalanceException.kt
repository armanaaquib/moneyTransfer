package com.bsf.moneyTransfer.exception

import com.bsf.moneyTransfer.model.Money

class InsufficientBalanceException(amount: Money) : Exception() {
    override val message: String = "Insufficient available balance to transfer ${amount.number}"
}
