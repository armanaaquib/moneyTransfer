package com.bsf.moneyTransfer.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
data class Account(
    @Column(unique = true)
    val accountNumber: String,

    @OneToOne(cascade = [CascadeType.ALL])
    val balance: Money = Money(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:JsonIgnore
    var id: Long? = null
)
