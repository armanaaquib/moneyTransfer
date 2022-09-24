package com.bsf.moneyTransfer.service

import com.bsf.moneyTransfer.exception.AccountNotFoundException
import com.bsf.moneyTransfer.model.Account
import com.bsf.moneyTransfer.repository.AccountRepository
import com.bsf.moneyTransfer.util.UniqueIdGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
internal class AccountServiceTest {

    @Mock
    private lateinit var accountRepository: AccountRepository

    @Mock
    private lateinit var uniqueIdGenerator: UniqueIdGenerator

    @InjectMocks
    private lateinit var accountService: AccountService

    @Test
    fun `should create account`() {
        val accountNumber = "account_number"
        val expectedAccount = Account(accountNumber)
        given(uniqueIdGenerator.generateId()).willReturn(accountNumber)
        given(accountRepository.save(expectedAccount)).willReturn(expectedAccount)

        val account = accountService.createAccount()

        assertEquals(expectedAccount, account)
        verify(accountRepository).save(expectedAccount)
    }

    @Test
    fun `should give account details when account is present`() {
        val accountNumber = "account_number"
        val expectedAccount = Account(accountNumber, BigDecimal(100))
        given(accountRepository.findByAccountNumber(accountNumber)).willReturn(expectedAccount)

        val account = accountService.getAccount(accountNumber)

        assertEquals(expectedAccount, account)
    }

    @Test
    fun `should throw account not found exception when account is not present`() {
        val accountNumber = "account_number"
        given(accountRepository.findByAccountNumber(accountNumber)).willReturn(null)

        val exception = assertThrows<AccountNotFoundException> {
            accountService.getAccount(accountNumber)
        }

        assertEquals("Could not find a account with $accountNumber account number", exception.message)
    }

    @Test
    fun `should update account`() {
        val expectedAccount = Account(accountNumber = "account_number", balance = BigDecimal(100))
        given(accountRepository.save(expectedAccount)).willReturn(expectedAccount)

        val account = accountService.updateAccount(expectedAccount)

        assertEquals(expectedAccount, account)
        verify(accountRepository).save(expectedAccount)
    }

}