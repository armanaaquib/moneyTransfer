package com.bsf.moneyTransfer.repository

import com.bsf.moneyTransfer.model.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findAllByAccountNumber(accountNumber: String): List<Transaction>
}
