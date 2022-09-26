package com.bsf.moneyTransfer.service

import com.bsf.moneyTransfer.exception.InsufficientBalanceException
import com.bsf.moneyTransfer.extension.compareTo
import com.bsf.moneyTransfer.extension.minus
import com.bsf.moneyTransfer.extension.plus
import com.bsf.moneyTransfer.model.Money
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TransactionService(private val accountService: AccountService) {

    @Transactional
    fun transferMoney(senderAccountNumber: String, receiverAccountNumber: String, amount: Money) {
        var senderAccount = accountService.getAccount(senderAccountNumber)
        var receiverAccount = accountService.getAccount(receiverAccountNumber)

        if (senderAccount.balance < amount) {
            throw InsufficientBalanceException(amount)
        }

        senderAccount = senderAccount.copy(balance = senderAccount.balance - amount)
        receiverAccount = receiverAccount.copy(balance = receiverAccount.balance + amount)

        accountService.updateAccount(senderAccount)
        accountService.updateAccount(receiverAccount)
    }

}
