package com.bsf.moneyTransfer.controller

import com.bsf.moneyTransfer.dto.ApiError
import com.bsf.moneyTransfer.dto.FailureResponse
import com.bsf.moneyTransfer.dto.MoneyTransferRequest
import com.bsf.moneyTransfer.exception.AccountNotFoundException
import com.bsf.moneyTransfer.exception.InsufficientBalanceException
import com.bsf.moneyTransfer.service.MoneyTransferService
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

@WebMvcTest(MoneyTransferController::class)
internal class MoneyTransferControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    private lateinit var moneyTransferService: MoneyTransferService

    @Test
    fun `should transfer money when sender account has enough balance to transfer`() {
        val senderAccountNumber = "sender_account"
        val receiverAccountNumber = "receiver_account"
        val moneyTransferRequest = MoneyTransferRequest(senderAccountNumber, receiverAccountNumber, BigDecimal(100))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/moneyTransfer")
                .content(asJsonString(moneyTransferRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNoContent)
        verify(moneyTransferService).transferMoney(
            senderAccountNumber,
            receiverAccountNumber,
            moneyTransferRequest.amount
        )
    }


    @Test
    fun `should give error when sender account does not have enough balance to transfer`() {
        val moneyTransferRequest = MoneyTransferRequest("sender_account", "receiver_account", BigDecimal(100))
        given(
            moneyTransferService.transferMoney(
                moneyTransferRequest.senderAccountNumber,
                moneyTransferRequest.receiverAccountNumber,
                moneyTransferRequest.amount
            )
        )
            .willThrow(InsufficientBalanceException(moneyTransferRequest.amount))
        val expectedResponse =
            FailureResponse(ApiError("Insufficient available balance to transfer ${moneyTransferRequest.amount}"))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/moneyTransfer")
                .content(asJsonString(moneyTransferRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().json(asJsonString(expectedResponse)))
    }

    @Test
    fun `should give error when any of the account ids is invalid`() {
        val moneyTransferRequest = MoneyTransferRequest("sender_account", "receiver_account", BigDecimal(100))
        given(
            moneyTransferService.transferMoney(
                moneyTransferRequest.senderAccountNumber,
                moneyTransferRequest.receiverAccountNumber,
                moneyTransferRequest.amount
            )
        )
            .willThrow(AccountNotFoundException(moneyTransferRequest.senderAccountNumber))
        val expectedResponse =
            FailureResponse(ApiError("Could not find a account with ${moneyTransferRequest.senderAccountNumber} account number"))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/moneyTransfer")
                .content(asJsonString(moneyTransferRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().json(asJsonString(expectedResponse)))
    }

}