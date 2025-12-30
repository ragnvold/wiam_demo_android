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
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import java.math.BigDecimal

@Composable
fun LoanAmountSliderSection(
    amount: BigDecimal,
    animatedAmount: BigDecimal,
    onAmountChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val resources = LocalResources.current
    val minAmount = resources.getInteger(R.integer.loan_min_amount_usd).toFloat()
    val maxAmount = resources.getInteger(R.integer.loan_max_amount_usd).toFloat()
    val step = resources.getInteger(R.integer.loan_amount_step_usd).toFloat()

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
                text = stringResource(R.string.loan_amount_title),
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = stringResource(
                    R.string.loan_currency_amount_format,
                    MoneyFormatter.formatUsdWhole(animatedAmount)
                ),
                style = MaterialTheme.typography.headlineSmall
            )

            Slider(
                value = amount.toFloat(),
                onValueChange = onAmountChanged,
                valueRange = minAmount..maxAmount,
                steps = ((maxAmount - minAmount) / step).toInt() - 1
            )
        }
    }
}
