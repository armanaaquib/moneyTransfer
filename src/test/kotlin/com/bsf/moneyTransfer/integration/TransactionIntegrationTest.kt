package com.bsf.moneyTransfer.integration

import com.bsf.moneyTransfer.dto.ApiError
import com.bsf.moneyTransfer.dto.FailureResponse
import com.bsf.moneyTransfer.dto.TransactionRequest
import com.bsf.moneyTransfer.dto.SuccessResponse
import com.bsf.moneyTransfer.model.Money
import com.bsf.moneyTransfer.model.Transaction
import com.bsf.moneyTransfer.model.TransactionType
import com.bsf.moneyTransfer.util.asJsonString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class TransactionIntegrationTest(@Autowired private val restTemplate: TestRestTemplate) {

    @Test
    fun `should transfer money`() {
        val transactionRequest = TransactionRequest("123", "124", Money(BigDecimal(10)))

        val entity = restTemplate.postForEntity("/transactions", transactionRequest, SuccessResponse::class.java)

        assertEquals(HttpStatus.NO_CONTENT, entity.statusCode)
    }

    @Test
    fun `should get error when sender does not have enough money`() {
        val transactionRequest = TransactionRequest("123", "124", Money(BigDecimal(110)))
        val expectedResponse =
            FailureResponse(ApiError("Insufficient available balance to transfer ${transactionRequest.amount.number}"))

        val entity = restTemplate.postForEntity("/transactions", transactionRequest, FailureResponse::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, entity.statusCode)
        assertEquals(expectedResponse, entity.body)
    }

    @Test
    fun `should get error when sender account id not valid`() {
        val transactionRequest = TransactionRequest("xxx", "124", Money(BigDecimal(10)))
        val expectedResponse =
            FailureResponse(error = ApiError("Could not find an account with ${transactionRequest.senderAccountNumber} account number"))

        val entity = restTemplate.postForEntity("/transactions", transactionRequest, FailureResponse::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, entity.statusCode)
        assertEquals(expectedResponse, entity.body)
    }


    @Test
    fun `should get error when receiver account id not valid`() {
        val transactionRequest = TransactionRequest("123", "xxx", Money(BigDecimal(10)))
        val expectedResponse =
            FailureResponse(error = ApiError("Could not find an account with ${transactionRequest.receiverAccountNumber} account number"))

        val entity = restTemplate.postForEntity("/transactions", transactionRequest, FailureResponse::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, entity.statusCode)
        assertEquals(expectedResponse, entity.body)
    }

    @Test
    fun `should return transactions for the given account number`() {
        val accountNumber = "123"
        val transaction1 = Transaction(accountNumber, Money(BigDecimal(100)), TransactionType.CREDIT)
        val expectedResponse = SuccessResponse(listOf(transaction1))

        val entity = restTemplate.getForEntity("/transactions/$accountNumber", String::class.java)

        assertEquals(HttpStatus.OK, entity.statusCode)
        JSONAssert.assertEquals(asJsonString(expectedResponse), entity.body, true)
    }

}
