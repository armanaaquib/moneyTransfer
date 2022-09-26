package com.bsf.moneyTransfer.exception

class AccountNotFoundException(accountNumber: String) : Exception() {
    override val message: String = "Could not find an account with $accountNumber account number"
}
