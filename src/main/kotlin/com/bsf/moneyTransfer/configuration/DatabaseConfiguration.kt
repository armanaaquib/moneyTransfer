package com.bsf.moneyTransfer.configuration

import com.bsf.moneyTransfer.model.Account
import com.bsf.moneyTransfer.model.Money
import com.bsf.moneyTransfer.model.Transaction
import com.bsf.moneyTransfer.model.TransactionType
import com.bsf.moneyTransfer.repository.AccountRepository
import com.bsf.moneyTransfer.repository.TransactionRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.math.BigDecimal

@Configuration
@Profile("test", "dev")
class DatabaseConfiguration {

    @Bean
    fun databaseInitializer(accountRepository: AccountRepository, transactionRepository: TransactionRepository) =
        ApplicationRunner {
            val accountNumber1 = "123"
            accountRepository.save(Account(accountNumber1, Money(BigDecimal(100))))
            transactionRepository.save(Transaction(accountNumber1, Money(BigDecimal(100)), TransactionType.CREDIT))

            val accountNumber2 = "124"
            accountRepository.save(Account(accountNumber2, Money(BigDecimal(10))))
            transactionRepository.save(Transaction(accountNumber2, Money(BigDecimal(10)), TransactionType.CREDIT))
        }

}
