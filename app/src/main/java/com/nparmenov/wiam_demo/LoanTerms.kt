package com.nparmenov.wiam_demo

import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class LoanTerms(
    @Serializable(with = BigDecimalAsStringSerializer::class)
    val loanAmount: BigDecimal = BigDecimal("5000"),
    val loanTerm: Int = 7
)