package com.nparmenov.wiam_demo

import java.math.BigDecimal

data class LoanCalculatorConfig(
    val minAmountUsd: BigDecimal,
    val maxAmountUsd: BigDecimal,
    val amountStepUsd: BigDecimal,
    val allowedTermsDays: Set<Int>
)
