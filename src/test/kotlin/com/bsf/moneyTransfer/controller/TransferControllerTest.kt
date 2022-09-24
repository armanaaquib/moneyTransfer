package com.bsf.moneyTransfer.controller

import com.bsf.moneyTransfer.dto.ApiError
import com.bsf.moneyTransfer.dto.ApiResponse
import com.bsf.moneyTransfer.dto.TransferRequest
import com.bsf.moneyTransfer.exception.AccountNotFoundException
import com.bsf.moneyTransfer.exception.InsufficientBalanceException
import com.bsf.moneyTransfer.service.TransferService
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

@WebMvcTest(TransferController::class)
internal class TransferControllerTest {

    @MockBean
    private lateinit var transferService: TransferService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should transfer money when sender account has enough balance to transfer`() {
        val senderAccountNumber = "sender_account"
        val receiverAccountNumber = "receiver_account"
        val transferRequest = TransferRequest(senderAccountNumber, receiverAccountNumber, BigDecimal(100))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/transfers")
                .content(asJsonString(transferRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
        verify(transferService).transferMoney(senderAccountNumber, receiverAccountNumber, transferRequest.amount)
    }


    @Test
    fun `should give error when sender account does not have enough balance to transfer`() {
        val transferRequest = TransferRequest("sender_account", "receiver_account", BigDecimal(100))
        given(
            transferService.transferMoney(
                transferRequest.senderAccountNumber,
                transferRequest.receiverAccountNumber,
                transferRequest.amount
            )
        )
            .willThrow(InsufficientBalanceException(transferRequest.amount))
        val expectedResponse =
            ApiResponse<Any>(error = ApiError("Insufficient available balance to transfer ${transferRequest.amount}"))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/transfers")
                .content(asJsonString(transferRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().json(asJsonString(expectedResponse)))
    }

    @Test
    fun `should give error when any of the account ids is invalid`() {
        val transferRequest = TransferRequest("sender_account", "receiver_account", BigDecimal(100))
        given(
            transferService.transferMoney(
                transferRequest.senderAccountNumber,
                transferRequest.receiverAccountNumber,
                transferRequest.amount
            )
        )
            .willThrow(AccountNotFoundException(transferRequest.senderAccountNumber))
        val expectedResponse =
            ApiResponse<Any>(error = ApiError("Could not find a account with ${transferRequest.senderAccountNumber} account number"))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/transfers")
                .content(asJsonString(transferRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().json(asJsonString(expectedResponse)))
    }

}