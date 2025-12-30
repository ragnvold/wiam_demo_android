package com.nparmenov.wiam_demo

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Calendar
import java.util.Date

class LoanCalculator {

    fun calculateLoanInterestPerPeriod(
        basePercent: BigDecimal,
        basePeriod: Int,
        targetPeriod: Int
    ): BigDecimal {
        return basePercent
            .divide(basePeriod.toBigDecimal(), 34, RoundingMode.HALF_UP)
            .multiply(targetPeriod.toBigDecimal()).setScale(34, RoundingMode.HALF_UP)
    }

    fun calculateLoanTerm(
        currentDate: Calendar,
        periodInDays: Int
    ): Date {
        val c = (currentDate.clone() as Calendar).apply {
            add(Calendar.DAY_OF_MONTH, periodInDays)
            add(Calendar.DAY_OF_MONTH, 1)

            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return c.time
    }

    fun calculateTotalRepayment(
        amount: BigDecimal,
        loanInterest: BigDecimal
    ): BigDecimal {
        val finalLoanInterest = BigDecimal("100").plus(loanInterest).divide(BigDecimal("100"), 34, RoundingMode.HALF_UP)
        return amount.multiply(finalLoanInterest).setScale(2, RoundingMode.HALF_UP)
    }
}