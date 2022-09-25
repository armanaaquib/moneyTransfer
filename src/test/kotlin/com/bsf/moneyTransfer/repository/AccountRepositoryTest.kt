package com.bsf.moneyTransfer.repository

import com.bsf.moneyTransfer.model.Account
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
internal class AccountRepositoryTest(
    @Autowired val entityManager: TestEntityManager,
    @Autowired val accountRepository: AccountRepository
) {

    @Test
    fun `should find account by account number`() {
        val accountNumber = "123"
        val account = Account(accountNumber)
        entityManager.persist(account)
        entityManager.flush()

        assertEquals(account, accountRepository.findByAccountNumber(accountNumber))
    }

}