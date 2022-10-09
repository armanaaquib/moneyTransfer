package com.bsf.moneyTransfer.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@EntityListeners(AuditingEntityListener::class)
data class Transaction(
    val accountNumber: String,

    @OneToOne(cascade = [CascadeType.ALL])
    val amount: Money,

    val type: TransactionType,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    var id: Long? = null,

    @CreatedDate
    var createdAt: LocalDateTime? = null
)
