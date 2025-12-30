package com.nparmenov.wiam_demo

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object MoneyFormatter {

    private val symbols = DecimalFormatSymbols(Locale.US)

    private val whole = DecimalFormat("#,##0", symbols)
    private val twoDecimals = DecimalFormat("#,##0.00", symbols)

    fun formatUsdWhole(value: BigDecimal): String = whole.format(value)

    fun formatUsd2(value: BigDecimal): String = twoDecimals.format(value)
}
