package com.bsf.moneyTransfer.model

import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Account(
    @Id
    val accountNumber: String,
    val balance: BigDecimal = BigDecimal(0)
)
