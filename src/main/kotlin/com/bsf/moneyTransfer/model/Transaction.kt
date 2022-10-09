package com.bsf.moneyTransfer.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*


@Entity
data class Transaction(
    val accountNumber: String,

    @OneToOne(cascade = [CascadeType.ALL])
    val amount: Money,

    val type: TransactionType,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:JsonIgnore
    var id: Long? = null,
)
