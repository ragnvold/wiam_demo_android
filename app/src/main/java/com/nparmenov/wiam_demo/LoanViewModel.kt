package com.nparmenov.wiam_demo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.abs

class LoanCalculatorViewModel(
    private val restoreLastLoanApplicationMiddleware: RestoreLastLoanApplicationMiddleware,
    private val saveLastLoanApplicationMiddleware: SaveLastLoanApplicationMiddleware,
    private val loanComputationMiddleware: LoanComputationMiddleware,
    private val submitLoanApplicationMiddleware: SubmitLoanApplicationMiddleware,
    private val validator: LoanApplicationValidator,
    private val config: LoanCalculatorConfig
) : ViewModel() {

    private val store = LoanStore(
        initialState = LoanCalculatorState(
            loanTerms = LoanTerms(
                loanAmount = config.minAmountUsd,
                loanTerm = config.allowedTermsDays.minOrNull() ?: 7
            ),
            validation = LoanApplicationValidation.Valid
        ),
        reducer = { state, action -> loanCalculatorReduce(state, action, validator) },
        middlewares = listOf(
            restoreLastLoanApplicationMiddleware,
            saveLastLoanApplicationMiddleware,
            loanComputationMiddleware,
            submitLoanApplicationMiddleware
        )
    )

    val state: StateFlow<LoanCalculatorState> = store.state

    init {
        val initial = store.state.value
        store.dispatch(LoanAction.LoanAmountChanged(initial.loanTerms.loanAmount))
    }

    fun onAmountChanged(value: Float) {
        val step = config.amountStepUsd.toInt()
        val snapped = (value.toInt() / step) * step
        store.dispatch(LoanAction.LoanAmountChanged(snapped.toBigDecimal()))
    }

    fun onTermChanged(value: Float) {
        val allowed = config.allowedTermsDays.sorted()
        val nearest = allowed.minByOrNull { abs(it - value.toInt()) } ?: allowed.first()
        store.dispatch(LoanAction.LoanTermChanged(nearest))
    }

    fun onSubmitApplicationClicked() {
        store.dispatch(LoanAction.SubmissionClicked)
    }

    fun onSubmitDismissClicked() {
        store.dispatch(LoanAction.SubmitDismissSuccess)
    }

    fun onRestoreLastLoanApplication() {
        store.dispatch(LoanAction.RestoreLastLoanApplicationStarted)
    }
}
