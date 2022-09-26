package com.bsf.moneyTransfer.model

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
data class Account(
    @Id
    val accountNumber: String,

    @OneToOne(cascade = [CascadeType.ALL])
    val balance: Money = Money(),
)
