package com.bsf.moneyTransfer.util

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UniqueIdGenerator {
    fun generateId() = UUID.randomUUID().toString()
}
