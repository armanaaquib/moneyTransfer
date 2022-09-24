package com.bsf.moneyTransfer.exception

class AccountNotFoundException(accountNumber: String) : Exception() {
    override val message: String = "Could not find a account with $accountNumber account number"
}
