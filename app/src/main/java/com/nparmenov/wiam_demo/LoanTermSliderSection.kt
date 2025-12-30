package com.nparmenov.wiam_demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalResources

@Composable
fun LoanTermSliderSection(
    termDays: Int,
    onTermChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val terms = LocalResources.current.getIntArray(R.array.loan_allowed_terms_days)
    val minTerm = terms.first().toFloat()
    val maxTerm = terms.last().toFloat()

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation))
    ) {
        Column(Modifier.padding(dimensionResource(R.dimen.section_padding))) {
            Text(
                text = stringResource(R.string.loan_term_title),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = pluralStringResource(R.plurals.loan_term_days, termDays, termDays),
                style = MaterialTheme.typography.headlineSmall
            )
            Slider(
                value = termDays.toFloat(),
                onValueChange = onTermChanged,
                valueRange = minTerm..maxTerm,
                steps = terms.size - 2
            )
        }
    }
}
