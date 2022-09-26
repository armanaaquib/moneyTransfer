package com.bsf.moneyTransfer.extension

import com.bsf.moneyTransfer.model.Money

operator fun Money.plus(money: Money) = Money(this.number + money.number)

operator fun Money.minus(money: Money) = Money(this.number - money.number)

operator fun Money.compareTo(money: Money): Int {
    return when {
        this.number < money.number -> -1
        this.number > money.number -> 1
        else -> 0
    }
}
