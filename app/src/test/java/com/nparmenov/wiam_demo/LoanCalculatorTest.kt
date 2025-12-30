package com.nparmenov.wiam_demo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Calendar
import java.util.TimeZone

class LoanCalculatorTest {

    private val calculator = LoanCalculator()

    @Test
    fun `calculateLoanInterestPerPeriod scales base percent linearly`() {
        val interest7 = calculator.calculateLoanInterestPerPeriod(
            basePercent = BigDecimal("15"),
            basePeriod = 14,
            targetPeriod = 7
        )

        // 15% for 14 days -> 7.5% for 7 days
        assertBigDecimalEquals(BigDecimal("7.5"), interest7)

        val interest28 = calculator.calculateLoanInterestPerPeriod(
            basePercent = BigDecimal("15"),
            basePeriod = 14,
            targetPeriod = 28
        )
        // 15% for 14 days -> 30% for 28 days
        assertBigDecimalEquals(BigDecimal("30"), interest28)
    }

    @Test
    fun `calculateTotalRepayment applies percent and rounds to two decimals`() {
        val total = calculator.calculateTotalRepayment(
            amount = BigDecimal("10000"),
            loanInterest = BigDecimal("7.5")
        )

        assertEquals(BigDecimal("10750.00"), total)

        val rounded = calculator.calculateTotalRepayment(
            amount = BigDecimal("1"),
            loanInterest = BigDecimal("33.333")
        )

        assertEquals(2, rounded.scale())
        assertEquals(BigDecimal("1.33"), rounded)
    }

    @Test
    fun `calculateLoanTerm returns midnight and adds period plus one day`() {
        val tz = TimeZone.getTimeZone("UTC")
        val base = Calendar.getInstance(tz).apply {
            set(2025, Calendar.JANUARY, 10, 13, 37, 59)
            set(Calendar.MILLISECOND, 123)
        }

        val result = calculator.calculateLoanTerm(base, 14)

        val expected = (base.clone() as Calendar).apply {
            add(Calendar.DAY_OF_MONTH, 14)
            add(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        assertEquals(expected.time, result.time)

        val check = Calendar.getInstance(tz).apply { time = result }
        assertTrue(check.get(Calendar.HOUR_OF_DAY) == 0)
        assertTrue(check.get(Calendar.MINUTE) == 0)
        assertTrue(check.get(Calendar.SECOND) == 0)
        assertTrue(check.get(Calendar.MILLISECOND) == 0)
    }

    private fun assertBigDecimalEquals(expected: BigDecimal, actual: BigDecimal) {
        val e = expected.stripTrailingZeros()
        val a = actual.stripTrailingZeros().setScale(e.scale().coerceAtLeast(0), RoundingMode.HALF_UP)
        assertEquals(0, a.compareTo(e))
    }
}
