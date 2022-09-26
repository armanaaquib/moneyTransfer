package com.bsf.moneyTransfer.controller

import com.bsf.moneyTransfer.dto.ApiError
import com.bsf.moneyTransfer.dto.FailureResponse
import com.bsf.moneyTransfer.dto.TransactionRequest
import com.bsf.moneyTransfer.exception.AccountNotFoundException
import com.bsf.moneyTransfer.exception.InsufficientBalanceException
import com.bsf.moneyTransfer.model.Money
import com.bsf.moneyTransfer.service.TransactionService
import com.bsf.moneyTransfer.util.asJsonString
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal

@WebMvcTest(TransactionController::class)
internal class TransactionControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    private lateinit var transactionService: TransactionService

    @Test
    fun `should transfer money when sender account has enough balance to transfer`() {
        val senderAccountNumber = "sender_account"
        val receiverAccountNumber = "receiver_account"
        val transactionRequest = TransactionRequest(senderAccountNumber, receiverAccountNumber, Money(BigDecimal(100)))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/transactions")
                .content(asJsonString(transactionRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNoContent)
        verify(transactionService).transferMoney(
            senderAccountNumber,
            receiverAccountNumber,
            transactionRequest.amount
        )
    }


    @Test
    fun `should give error when sender account does not have enough balance to transfer`() {
        val transactionRequest = TransactionRequest("sender_account", "receiver_account", Money(BigDecimal(100)))
        given(
            transactionService.transferMoney(
                transactionRequest.senderAccountNumber,
                transactionRequest.receiverAccountNumber,
                transactionRequest.amount
            )
        )
            .willAnswer { throw InsufficientBalanceException(transactionRequest.amount) }
        val expectedResponse =
            FailureResponse(ApiError("Insufficient available balance to transfer ${transactionRequest.amount.number}"))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/transactions")
                .content(asJsonString(transactionRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().json(asJsonString(expectedResponse)))
    }

    @Test
    fun `should give error when any of the account ids is invalid`() {
        val transactionRequest = TransactionRequest("sender_account", "receiver_account", Money(BigDecimal(100)))
        given(
            transactionService.transferMoney(
                transactionRequest.senderAccountNumber,
                transactionRequest.receiverAccountNumber,
                transactionRequest.amount
            )
        )
            .willAnswer { throw AccountNotFoundException(transactionRequest.senderAccountNumber) }
        val expectedResponse =
            FailureResponse(ApiError("Could not find an account with ${transactionRequest.senderAccountNumber} account number"))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/transactions")
                .content(asJsonString(transactionRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().json(asJsonString(expectedResponse)))
    }

}