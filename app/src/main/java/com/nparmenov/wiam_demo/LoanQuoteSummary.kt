package com.nparmenov.wiam_demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import java.math.RoundingMode
import java.text.DateFormat

@Composable
fun LoanQuoteSummary(
    quote: LoanQuote,
    modifier: Modifier = Modifier
) {
    val dateText = DateFormat.getDateInstance(DateFormat.MEDIUM)
        .format(quote.dueDate)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation))
    ) {
        Column(Modifier.padding(dimensionResource(R.dimen.section_padding))) {
            Text(
                text = stringResource(R.string.loan_quote_title),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.quote_item_spacing)))
            Text(
                text = stringResource(
                    R.string.loan_apr_format,
                    quote.loanInterestPerPeriod
                        .setScale(2, RoundingMode.HALF_UP)
                        .toPlainString()
                ),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.quote_item_spacing)))
            Text(
                text = stringResource(
                    R.string.loan_total_repayment_format,
                    MoneyFormatter.formatUsd2(quote.totalRepayment)
                ),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.quote_item_spacing)))
            Text(
                text = stringResource(R.string.loan_due_date_format, dateText),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
