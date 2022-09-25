package com.bsf.moneyTransfer.configuration

import com.bsf.moneyTransfer.model.Account
import com.bsf.moneyTransfer.repository.AccountRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.math.BigDecimal

@Configuration
@Profile("test", "dev")
class DatabaseConfiguration {

    @Bean
    fun databaseInitializer(accountRepository: AccountRepository) = ApplicationRunner {
        accountRepository.save(Account(accountNumber = "123", balance = BigDecimal(100)))
        accountRepository.save(Account(accountNumber = "124", balance = BigDecimal(10)))
    }

}
