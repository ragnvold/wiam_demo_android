package com.nparmenov.wiam_demo

import java.math.BigDecimal
import java.util.Date

data class LoanCalculatorState(
    val loanTerms: LoanTerms = LoanTerms(),
    val validation: LoanApplicationValidation = LoanApplicationValidation.Valid,
    val loanQuote: LoanQuote? = null,
    val submission: SubmissionState = SubmissionState.Idle
)

data class LoanQuote(
    val loanInterestPerPeriod: BigDecimal,
    val totalRepayment: BigDecimal,
    val dueDate: Date,
)
