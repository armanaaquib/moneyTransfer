package com.bsf.moneyTransfer.service

import com.bsf.moneyTransfer.exception.AccountNotFoundException
import com.bsf.moneyTransfer.exception.InsufficientBalanceException
import org.springframework.stereotype.Service
import java.math.BigDecimal
import kotlin.jvm.Throws

@Service
class TransactionService(private val accountService: AccountService) {

    @Throws(InsufficientBalanceException::class, AccountNotFoundException::class)
    fun transferMoney(senderAccountNumber: String, receiverAccountNumber: String, amount: BigDecimal) {
        var senderAccount = accountService.getAccount(senderAccountNumber)
        var receiverAccount = accountService.getAccount(receiverAccountNumber)

        if(senderAccount.balance < amount) {
            throw InsufficientBalanceException(amount)
        }

        senderAccount = senderAccount.copy(balance = senderAccount.balance - amount)
        receiverAccount = receiverAccount.copy(balance = receiverAccount.balance + amount)

        accountService.updateAccount(senderAccount)
        accountService.updateAccount(receiverAccount)
    }

}