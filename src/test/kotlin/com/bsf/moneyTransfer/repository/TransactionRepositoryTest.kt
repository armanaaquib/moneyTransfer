package com.bsf.moneyTransfer.repository

import com.bsf.moneyTransfer.model.Money
import com.bsf.moneyTransfer.model.Transaction
import com.bsf.moneyTransfer.model.TransactionType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.math.BigDecimal

@DataJpaTest
internal class TransactionRepositoryTest(
    @Autowired val entityManager: TestEntityManager,
    @Autowired val transactionRepository: TransactionRepository
) {

    @Test
    fun `should find account by account number`() {
        val accountNumber = "123"
        val transaction1 = Transaction(accountNumber, Money(BigDecimal(100)), TransactionType.CREDIT)
        entityManager.persist(transaction1)
        val transaction2 = Transaction(accountNumber, Money(BigDecimal(20)), TransactionType.DEBIT)
        entityManager.persist(transaction2)
        entityManager.flush()

        Assertions.assertEquals(
            listOf(transaction2, transaction1),
            transactionRepository.findAllByAccountNumberOrderByCreatedAtDesc(accountNumber)
        )
    }
}