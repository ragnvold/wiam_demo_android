package com.nparmenov.wiam_demo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal

class LoanCalculatorReducerTest {

    private val validator = LoanApplicationValidator(
        LoanCalculatorConfig(
            minAmountUsd = BigDecimal(5000),
            maxAmountUsd = BigDecimal(50000),
            amountStepUsd = BigDecimal(5000),
            allowedTermsDays = setOf(7, 14, 21, 28)
        )
    )

    @Test
    fun `LoanAmountChanged updates terms and validation`() {
        val initial = LoanCalculatorState(
            loanTerms = LoanTerms(loanAmount = BigDecimal("5000"), loanTerm = 7),
            validation = LoanApplicationValidation.Valid
        )

        val next = loanCalculatorReduce(
            state = initial,
            action = LoanAction.LoanAmountChanged(BigDecimal("4999")),
            validator = validator
        )

        assertEquals(BigDecimal("4999"), next.loanTerms.loanAmount)
        assertTrue(next.validation is LoanApplicationValidation.Invalid)
    }

    @Test
    fun `LastLoanApplicationRestored updates terms and validation`() {
        val initial = LoanCalculatorState(
            validation = LoanApplicationValidation.Valid
        )

        val restored = LoanTerms(loanAmount = BigDecimal("50000"), loanTerm = 28)

        val next = loanCalculatorReduce(
            state = initial,
            action = LoanAction.LastLoanApplicationRestored(restored),
            validator = validator
        )

        assertEquals(restored, next.loanTerms)
        assertTrue(next.validation is LoanApplicationValidation.Valid)
    }
}
