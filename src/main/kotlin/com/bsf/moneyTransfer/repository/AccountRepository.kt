package com.bsf.moneyTransfer.repository

import com.bsf.moneyTransfer.model.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository: JpaRepository<Account, String> {
    fun findByAccountNumber(number: String): Account?
}