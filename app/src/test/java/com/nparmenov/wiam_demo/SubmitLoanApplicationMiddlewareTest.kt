package com.nparmenov.wiam_demo

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal
import java.util.Date

class SubmitLoanApplicationMiddlewareTest {

    private val validator = LoanApplicationValidator(
        LoanCalculatorConfig(
            minAmountUsd = BigDecimal(5000),
            maxAmountUsd = BigDecimal(50000),
            amountStepUsd = BigDecimal(5000),
            allowedTermsDays = setOf(7, 14, 21, 28)
        )
    )

    @Test
    fun `does not submit when calculation result is missing`() = runTest {
        val repo = FakeLoanRepository()
        val middleware = SubmitLoanApplicationMiddleware(repo, validator, this)

        val state = LoanCalculatorState(
            loanTerms = LoanTerms(loanAmount = BigDecimal("5000"), loanTerm = 7),
            validation = LoanApplicationValidation.Valid,
            loanQuote = null
        )

        val dispatched = mutableListOf<LoanAction>()

        middleware.intercept(
            state = state,
            action = LoanAction.SubmissionClicked,
            dispatch = { dispatched += it }
        )

        assertEquals(0, repo.calls)
        val failed = dispatched.filterIsInstance<LoanAction.SubmitFailed>().firstOrNull()
        assertTrue(failed != null)
        assertTrue(failed?.cause is ValidationThrowable)
    }

    @Test
    fun `submits calculated totalRepayment`() = runTest {
        val repo = FakeLoanRepository()
        val middleware = SubmitLoanApplicationMiddleware(repo, validator, this)

        val state = LoanCalculatorState(
            loanTerms = LoanTerms(loanAmount = BigDecimal("5000"), loanTerm = 7),
            validation = LoanApplicationValidation.Valid,
            loanQuote = LoanQuote(loanInterestPerPeriod = BigDecimal("15"), totalRepayment = BigDecimal("12345.67"), dueDate = Date())
        )

        val dispatched = mutableListOf<LoanAction>()

        middleware.intercept(
            state = state,
            action = LoanAction.SubmissionClicked,
            dispatch = { dispatched += it }
        )

        // Let coroutine run
        this.testScheduler.advanceUntilIdle()

        assertEquals(1, repo.calls)
        assertEquals(12346, repo.lastRequest?.totalRepayment)
        assertTrue(dispatched.any { it is LoanAction.SubmitStarted })
        assertTrue(dispatched.any { it is LoanAction.SubmitSucceeded })
    }

    private class FakeLoanRepository : LoanRepository {
        var calls: Int = 0
        var lastRequest: LoanApplicationRequest? = null

        override suspend fun submitApplication(req: LoanApplicationRequest): LoanApplicationResponse {
            calls++
            lastRequest = req
            return LoanApplicationResponse(
                id = 101,
                amount = req.amount,
                period = req.period,
                totalRepayment = req.totalRepayment
            )
        }
    }
}
