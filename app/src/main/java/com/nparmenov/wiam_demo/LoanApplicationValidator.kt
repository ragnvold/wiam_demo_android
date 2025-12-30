package com.nparmenov.wiam_demo

import androidx.annotation.StringRes

sealed interface LoanApplicationValidation {
    data object Valid : LoanApplicationValidation
    data class Invalid(
        @StringRes val messageResId: Int,
        val formatArgs: List<Any> = emptyList()
    ) : LoanApplicationValidation
}

class LoanApplicationValidator(
    private val config: LoanCalculatorConfig
) {

    fun validateTerms(params: LoanTerms): LoanApplicationValidation {
        if (params.loanAmount < config.minAmountUsd || params.loanAmount > config.maxAmountUsd) {
            return LoanApplicationValidation.Invalid(
                messageResId = R.string.validation_amount_range_format,
                formatArgs = listOf(config.minAmountUsd.toInt(), config.maxAmountUsd.toInt())
            )
        }
        if (params.loanAmount.stripTrailingZeros().scale() > 0) {
            return LoanApplicationValidation.Invalid(R.string.validation_amount_whole)
        }
        if (params.loanTerm !in config.allowedTermsDays) {
            return LoanApplicationValidation.Invalid(R.string.validation_term_allowed)
        }
        return LoanApplicationValidation.Valid
    }

    fun validateReadyToSubmit(state: LoanCalculatorState): LoanApplicationValidation {
        val termsValidation = validateTerms(state.loanTerms)
        if (termsValidation is LoanApplicationValidation.Invalid) return termsValidation
        if (state.loanQuote == null) return LoanApplicationValidation.Invalid(R.string.validation_wait_for_quote)
        return LoanApplicationValidation.Valid
    }
}
