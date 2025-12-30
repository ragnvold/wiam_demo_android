package com.nparmenov.wiam_demo

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource

@Composable
fun LoanCalculatorScreen(
    viewModel: LoanCalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val state = viewModel.state.collectAsState().value
    val terms = state.loanTerms

    val snackbarHostState = remember { SnackbarHostState() }

    val animatedLoanAmount by animateFloatAsState(
        targetValue = terms.loanAmount.toFloat(),
        label = "loanAmount"
    )

    val snackbarMessage: String? = when (val submission = state.submission) {
        is SubmissionState.Success -> {
            val id = submission.requestId?.toIntOrNull()
            if (id != null) {
                stringResource(R.string.loan_snackbar_submitted_with_id, id)
            } else {
                stringResource(R.string.loan_snackbar_submitted)
            }
        }

        is SubmissionState.Error -> {
            when (val cause = submission.cause) {
                is ValidationThrowable -> stringResource(
                    cause.validation.messageResId,
                    *cause.validation.formatArgs.toTypedArray()
                )
                else -> submission.message
            }
        }

        else -> null
    }

    LaunchedEffect(Unit) {
        viewModel.onRestoreLastLoanApplication()
    }

    LaunchedEffect(state.submission, snackbarMessage) {
        val msg = snackbarMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(msg)
        viewModel.onSubmitDismissClicked()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = modifier.padding(horizontal = dimensionResource(R.dimen.screen_padding_horizontal))
            ) {
                Spacer(modifier = modifier.height(dimensionResource(R.dimen.screen_padding_top)))

                LoanAmountSliderSection(
                    amount = terms.loanAmount,
                    animatedAmount = animatedLoanAmount.toBigDecimal(),
                    onAmountChanged = viewModel::onAmountChanged,
                )

                Spacer(modifier = modifier.height(dimensionResource(R.dimen.section_spacing)))

                LoanTermSliderSection(
                    termDays = terms.loanTerm,
                    onTermChanged = viewModel::onTermChanged
                )

                Spacer(modifier = modifier.height(dimensionResource(R.dimen.section_spacing)))

                state.loanQuote?.let { quote ->
                    LoanQuoteSummary(quote = quote)
                    Spacer(modifier = modifier.height(dimensionResource(R.dimen.section_spacing)))
                }

                Button(
                    onClick = viewModel::onSubmitApplicationClicked,
                    enabled = state.validation is LoanApplicationValidation.Valid && state.submission !is SubmissionState.Loading
                ) {
                    Text(text = stringResource(R.string.loan_submit_button))
                }

                if (state.validation is LoanApplicationValidation.Invalid) {
                    Spacer(modifier = modifier.height(dimensionResource(R.dimen.section_spacing)))
                    val invalid = state.validation
                    Text(
                        text = stringResource(invalid.messageResId, *invalid.formatArgs.toTypedArray()),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            if (state.submission is SubmissionState.Loading) {
                FullScreenLoading(modifier = modifier)
            }
        }
    }
}
