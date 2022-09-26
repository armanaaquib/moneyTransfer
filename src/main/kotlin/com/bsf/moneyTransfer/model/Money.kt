package com.bsf.moneyTransfer.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Money(
    val number: BigDecimal = BigDecimal(0),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:JsonIgnore
    var id: Long? = null
)
