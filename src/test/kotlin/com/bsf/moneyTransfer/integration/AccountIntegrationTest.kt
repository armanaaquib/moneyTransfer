package com.bsf.moneyTransfer.integration

import com.bsf.moneyTransfer.dto.ApiError
import com.bsf.moneyTransfer.dto.FailureResponse
import com.bsf.moneyTransfer.dto.SuccessResponse
import com.bsf.moneyTransfer.model.Account
import com.bsf.moneyTransfer.util.UniqueIdGenerator
import com.bsf.moneyTransfer.util.asJsonString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import java.math.BigDecimal

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class AccountIntegrationTest(@Autowired private val restTemplate: TestRestTemplate) {

    @MockBean
    private lateinit var idGenerator: UniqueIdGenerator

    @Test
    fun `should create account`() {
        val accountNumber = "125"
        given(idGenerator.generateId()).willReturn(accountNumber)
        val expectedResponse = SuccessResponse(Account(accountNumber, BigDecimal(0)))

        val entity = restTemplate.postForEntity("/accounts", null, String::class.java)

        assertEquals(HttpStatus.CREATED, entity.statusCode)
        assertTrue(entity.headers.location.toString().contains("/accounts/$accountNumber"))
        JSONAssert.assertEquals(asJsonString(expectedResponse), entity.body, false)
    }

    @Test
    fun `should get account when account is present`() {
        val accountNumber = "123"
        val expectedResponse = SuccessResponse(Account(accountNumber, BigDecimal(100)))

        val entity = restTemplate.getForEntity("/accounts/123", String::class.java)

        assertEquals(HttpStatus.OK, entity.statusCode)
        JSONAssert.assertEquals(asJsonString(expectedResponse), entity.body, false)
    }

    @Test
    fun `should get error when account is not present`() {
        val accountNumber = "xxx"
        val expectedResponse =
            FailureResponse(error = ApiError("Could not find a account with $accountNumber account number"))

        val entity = restTemplate.getForEntity("/accounts/$accountNumber", String::class.java)

        assertEquals(HttpStatus.NOT_FOUND, entity.statusCode)
        JSONAssert.assertEquals(asJsonString(expectedResponse), entity.body, false)
    }

}