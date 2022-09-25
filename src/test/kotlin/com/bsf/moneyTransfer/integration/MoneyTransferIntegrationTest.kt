package com.bsf.moneyTransfer.integration

import com.bsf.moneyTransfer.dto.ApiError
import com.bsf.moneyTransfer.dto.FailureResponse
import com.bsf.moneyTransfer.dto.MoneyTransferRequest
import com.bsf.moneyTransfer.dto.SuccessResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import java.math.BigDecimal

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class MoneyTransferIntegrationTest(@Autowired private val restTemplate: TestRestTemplate) {

    @Test
    fun `should transfer money`() {
        val moneyTransferRequest = MoneyTransferRequest("123", "124", BigDecimal(10))

        val entity = restTemplate.postForEntity("/moneyTransfers", moneyTransferRequest, SuccessResponse::class.java)

        assertEquals(HttpStatus.NO_CONTENT, entity.statusCode)
    }

    @Test
    fun `should get error when sender does not have enough money`() {
        val moneyTransferRequest = MoneyTransferRequest("123", "124", BigDecimal(110))
        val expectedResponse =
            FailureResponse(ApiError("Insufficient available balance to transfer ${moneyTransferRequest.amount}"))

        val entity = restTemplate.postForEntity("/moneyTransfers", moneyTransferRequest, FailureResponse::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, entity.statusCode)
        assertEquals(expectedResponse, entity.body)
    }

    @Test
    fun `should get error when sender account id not valid`() {
        val moneyTransferRequest = MoneyTransferRequest("xxx", "124", BigDecimal(10))
        val expectedResponse =
            FailureResponse(error = ApiError("Could not find a account with ${moneyTransferRequest.senderAccountNumber} account number"))

        val entity = restTemplate.postForEntity("/moneyTransfers", moneyTransferRequest, FailureResponse::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, entity.statusCode)
        assertEquals(expectedResponse, entity.body)
    }


    @Test
    fun `should get error when receiver account id not valid`() {
        val moneyTransferRequest = MoneyTransferRequest("123", "xxx", BigDecimal(10))
        val expectedResponse =
            FailureResponse(error = ApiError("Could not find a account with ${moneyTransferRequest.receiverAccountNumber} account number"))

        val entity = restTemplate.postForEntity("/moneyTransfers", moneyTransferRequest, FailureResponse::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, entity.statusCode)
        assertEquals(expectedResponse, entity.body)
    }

}
