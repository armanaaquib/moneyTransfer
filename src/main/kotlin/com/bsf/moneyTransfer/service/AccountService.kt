package com.bsf.moneyTransfer.service

import com.bsf.moneyTransfer.exception.AccountNotFoundException
import com.bsf.moneyTransfer.model.Account
import com.bsf.moneyTransfer.repository.AccountRepository
import com.bsf.moneyTransfer.util.UniqueIdGenerator
import org.springframework.stereotype.Service

@Service
class AccountService(private val idGenerator: UniqueIdGenerator, private val repository: AccountRepository) {

    fun createAccount(): Account {
        val account = Account(accountNumber = idGenerator.generateId())
        return repository.save(account)
    }

    fun getAccount(accountNumber: String): Account {
        return repository.findByAccountNumber(accountNumber) ?: throw AccountNotFoundException(accountNumber)
    }

    fun updateAccount(account: Account) = repository.save(account)

}
