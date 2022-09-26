package com.bsf.moneyTransfer.extension

import com.bsf.moneyTransfer.model.Money
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.math.BigDecimal

internal class MoneyExtensionsKtTest {

    @Test
    fun plus() {
        val money1 = Money(BigDecimal(10))
        val money2 = Money(BigDecimal(12))

        assertEquals(Money(BigDecimal(22)), money1 + money2)
    }

    @Test
    fun minus() {
        val money1 = Money(BigDecimal(100))
        val money2 = Money(BigDecimal(12))

        assertEquals(Money(BigDecimal(88)), money1 - money2)
    }

    @Test
    fun compareTo() {
        val money1 = Money(BigDecimal(10))
        val money2 = Money(BigDecimal(12))

        assertTrue(money1 < money2)
        assertFalse(money2 < money1)
    }
}