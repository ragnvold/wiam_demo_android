package com.nparmenov.wiam_demo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal

class LoanApplicationValidatorTest {

    private val config = LoanCalculatorConfig(
        minAmountUsd = BigDecimal(5000),
        maxAmountUsd = BigDecimal(50000),
        amountStepUsd = BigDecimal(5000),
        allowedTermsDays = setOf(7, 14, 21, 28)
    )

    private val validator = LoanApplicationValidator(config)

    @Test
    fun `validateTerms accepts valid amount and allowed term`() {
        val params = LoanTerms(loanAmount = BigDecimal("5000"), loanTerm = 14)
        val result = validator.validateTerms(params)
        assertTrue(result is LoanApplicationValidation.Valid)
    }

    @Test
    fun `validateTerms rejects amount outside range`() {
        val params = LoanTerms(loanAmount = BigDecimal("1"), loanTerm = 7)
        val result = validator.validateTerms(params)
        assertTrue(result is LoanApplicationValidation.Invalid)
        assertEquals(R.string.validation_amount_range_format, (result as LoanApplicationValidation.Invalid).messageResId)
    }

    @Test
    fun `validateReadyToSubmit requires calculation result`() {
        val state = LoanCalculatorState(
            loanTerms = LoanTerms(loanAmount = BigDecimal("5000"), loanTerm = 7),
            validation = LoanApplicationValidation.Valid,
            loanQuote = null
        )

        val validation = validator.validateReadyToSubmit(state)
        assertTrue(validation is LoanApplicationValidation.Invalid)
        assertEquals(R.string.validation_wait_for_quote, (validation as LoanApplicationValidation.Invalid).messageResId)
    }
}
