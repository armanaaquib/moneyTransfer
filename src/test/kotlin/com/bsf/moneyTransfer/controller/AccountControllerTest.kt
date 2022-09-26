package com.bsf.moneyTransfer.controller

import com.bsf.moneyTransfer.dto.ApiError
import com.bsf.moneyTransfer.dto.FailureResponse
import com.bsf.moneyTransfer.dto.SuccessResponse
import com.bsf.moneyTransfer.exception.AccountNotFoundException
import com.bsf.moneyTransfer.model.Account
import com.bsf.moneyTransfer.service.AccountService
import com.bsf.moneyTransfer.util.asJsonString
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigDecimal

@WebMvcTest(AccountController::class)
internal class AccountControllerTest(@Autowired private val mockMvc: MockMvc) {

    @MockBean
    private lateinit var accountService: AccountService

    @Test
    fun `should create account`() {
        val accountNumber = "123"
        given(accountService.createAccount()).willReturn(Account(accountNumber))
        val expectedResponse = SuccessResponse(Account(accountNumber, BigDecimal(0)))

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts"))
            .andExpect(status().isCreated)
            .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/accounts/$accountNumber"))
            .andExpect(content().json(asJsonString(expectedResponse)))
    }

    @Test
    fun `should get account when account is present`() {
        val accountNumber = "123"
        given(accountService.getAccount(accountNumber)).willReturn(Account(accountNumber))
        val expectedResponse = SuccessResponse(Account(accountNumber, BigDecimal(0)))

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/$accountNumber"))
            .andExpect(status().isOk)
            .andExpect(content().json(asJsonString(expectedResponse)))
    }

    @Test
    fun `should get error when account is not present`() {
        val accountNumber = "123"
        given(accountService.getAccount(accountNumber)).willAnswer { throw AccountNotFoundException(accountNumber) }
        val expectedResponse =
            FailureResponse(ApiError("Could not find a account with $accountNumber account number"))

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/$accountNumber"))
            .andExpect(status().isNotFound)
            .andExpect(content().json(asJsonString(expectedResponse)))
    }

}
