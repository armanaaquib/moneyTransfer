package com.bsf.moneyTransfer.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.Min

@Entity
data class Money(
    @field:Min(value = 0, message = "Amount can't be negative")
    val number: BigDecimal = BigDecimal(0),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    var id: Long? = null
)
