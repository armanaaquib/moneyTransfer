package com.bsf.moneyTransfer.service

import com.bsf.moneyTransfer.exception.AccountNotFoundException
import com.bsf.moneyTransfer.exception.InsufficientBalanceException
import com.bsf.moneyTransfer.model.Account
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
internal class TransactionServiceTest {

    @Mock
    private lateinit var accountService: AccountService

    @InjectMocks
    private lateinit var transactionService: TransactionService

    @Test
    fun `should transfer money when sender account has enough balance to transfer`() {
        val senderAccountNumber = "sender_account"
        val senderAccount = Account(accountNumber = senderAccountNumber, balance = BigDecimal(100))
        val receiverAccountNumber = "receiver_account"
        val receiverAccount = Account(accountNumber = receiverAccountNumber, balance = BigDecimal(100))
        given(accountService.getAccount(senderAccountNumber)).willReturn(senderAccount)
        given(accountService.getAccount(receiverAccountNumber)).willReturn(receiverAccount)

        transactionService.transferMoney(senderAccountNumber, receiverAccountNumber, BigDecimal(10))

        verify(accountService).updateAccount(senderAccount.copy(balance = BigDecimal(90)))
        verify(accountService).updateAccount(receiverAccount.copy(balance = BigDecimal(110)))
    }


    @Test
    fun `should throw insufficient balance exception when sender account does not have enough balance to transfer`() {
        val senderAccountNumber = "sender_account"
        val transferAmount = BigDecimal(100)
        val senderAccount = Account(accountNumber = senderAccountNumber, balance = BigDecimal(10))
        val receiverAccountNumber = "receiver_account"
        given(accountService.getAccount(senderAccountNumber)).willReturn(senderAccount)

        val exception = assertThrows<InsufficientBalanceException> {
            transactionService.transferMoney(senderAccountNumber, receiverAccountNumber, transferAmount)
        }

        assertEquals("Insufficient available balance to transfer $transferAmount", exception.message)
        verify(accountService).getAccount(senderAccountNumber)
        verify(accountService).getAccount(receiverAccountNumber)
        verifyNoMoreInteractions(accountService)
    }

    @Test
    fun `should throw account not found exception when sender account number is not valid`() {
        val senderAccountNumber = "sender_account"
        val receiverAccountNumber = "receiver_account"
        given(accountService.getAccount(senderAccountNumber)).willThrow(AccountNotFoundException(senderAccountNumber))

        val exception = assertThrows<AccountNotFoundException> {
            transactionService.transferMoney(senderAccountNumber, receiverAccountNumber, BigDecimal(100))
        }

        assertEquals("Could not find a account with $senderAccountNumber account number", exception.message)
    }

    @Test
    fun `should throw account not found exception when receiver account number is not valid`() {
        val senderAccountNumber = "sender_account"
        val receiverAccountNumber = "receiver_account"
        given(accountService.getAccount(senderAccountNumber))
            .willReturn(Account(accountNumber = senderAccountNumber, balance = BigDecimal(100)))
        given(accountService.getAccount(receiverAccountNumber))
            .willThrow(AccountNotFoundException(receiverAccountNumber))


        val exception = assertThrows<AccountNotFoundException> {
            transactionService.transferMoney(senderAccountNumber, receiverAccountNumber, BigDecimal(10))
        }

        assertEquals("Could not find a account with $receiverAccountNumber account number", exception.message)
    }

}