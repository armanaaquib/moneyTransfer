package com.bsf.moneyTransfer.service

import com.bsf.moneyTransfer.exception.AccountNotFoundException
import com.bsf.moneyTransfer.exception.InsufficientBalanceException
import com.bsf.moneyTransfer.model.Account
import com.bsf.moneyTransfer.model.Money
import com.bsf.moneyTransfer.model.Transaction
import com.bsf.moneyTransfer.model.TransactionType
import com.bsf.moneyTransfer.repository.TransactionRepository
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

    @Mock
    private lateinit var transactionRepository: TransactionRepository

    @InjectMocks
    private lateinit var transactionService: TransactionService

    @Test
    fun `should transfer money when sender account has enough balance to transfer`() {
        val senderAccountNumber = "sender_account"
        val senderAccount = Account(accountNumber = senderAccountNumber, balance = Money(BigDecimal(100)))
        val receiverAccountNumber = "receiver_account"
        val receiverAccount = Account(accountNumber = receiverAccountNumber, balance = Money(BigDecimal(100)))
        given(accountService.getAccount(senderAccountNumber)).willReturn(senderAccount)
        given(accountService.getAccount(receiverAccountNumber)).willReturn(receiverAccount)

        val amount = Money(BigDecimal(10))
        transactionService.transferMoney(senderAccountNumber, receiverAccountNumber, amount)

        verify(transactionRepository).save(Transaction(senderAccountNumber, amount, TransactionType.DEBIT))
        verify(transactionRepository).save(Transaction(receiverAccountNumber, amount, TransactionType.CREDIT))
        verify(accountService).updateAccount(senderAccount.copy(balance = Money(BigDecimal(90))))
        verify(accountService).updateAccount(receiverAccount.copy(balance = Money(BigDecimal(110))))
    }


    @Test
    fun `should throw insufficient balance exception when sender account does not have enough balance to transfer`() {
        val senderAccountNumber = "sender_account"
        val transferAmount = Money(BigDecimal(100))
        val senderAccount = Account(accountNumber = senderAccountNumber, balance = Money(BigDecimal(10)))
        val receiverAccountNumber = "receiver_account"
        given(accountService.getAccount(senderAccountNumber)).willReturn(senderAccount)

        val exception = assertThrows<InsufficientBalanceException> {
            transactionService.transferMoney(senderAccountNumber, receiverAccountNumber, transferAmount)
        }

        assertEquals("Insufficient available balance to transfer ${transferAmount.number}", exception.message)
        verify(accountService).getAccount(senderAccountNumber)
        verify(accountService).getAccount(receiverAccountNumber)
        verifyNoMoreInteractions(accountService)
    }

    @Test
    fun `should throw account not found exception when sender account number is not valid`() {
        val senderAccountNumber = "sender_account"
        val receiverAccountNumber = "receiver_account"
        given(accountService.getAccount(senderAccountNumber))
            .willAnswer { throw AccountNotFoundException(senderAccountNumber) }

        val exception = assertThrows<AccountNotFoundException> {
            transactionService.transferMoney(senderAccountNumber, receiverAccountNumber, Money(BigDecimal(100)))
        }

        assertEquals("Could not find an account with $senderAccountNumber account number", exception.message)
    }

    @Test
    fun `should throw account not found exception when receiver account number is not valid`() {
        val senderAccountNumber = "sender_account"
        val receiverAccountNumber = "receiver_account"
        given(accountService.getAccount(senderAccountNumber))
            .willReturn(Account(accountNumber = senderAccountNumber, balance = Money(BigDecimal(100))))
        given(accountService.getAccount(receiverAccountNumber))
            .willAnswer { throw AccountNotFoundException(receiverAccountNumber) }

        val exception = assertThrows<AccountNotFoundException> {
            transactionService.transferMoney(senderAccountNumber, receiverAccountNumber, Money(BigDecimal(10)))
        }

        assertEquals("Could not find an account with $receiverAccountNumber account number", exception.message)
    }

    @Test
    fun `should return all the transactions for the given account number`() {
        val accountNumber = "123"
        val transaction1 = Transaction(accountNumber, Money(BigDecimal(100)), TransactionType.CREDIT)
        val transaction2 = Transaction(accountNumber, Money(BigDecimal(20)), TransactionType.DEBIT)
        val expectedTransactions = listOf(transaction1, transaction2)
        given(transactionRepository.findAllByAccountNumberOrderByCreatedAtDesc(accountNumber)).willReturn(
            expectedTransactions
        )

        val transactions = transactionService.getTransactions(accountNumber)

        assertEquals(expectedTransactions, transactions)
    }

}