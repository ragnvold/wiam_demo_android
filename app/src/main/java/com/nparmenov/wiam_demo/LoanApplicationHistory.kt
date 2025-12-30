package com.nparmenov.wiam_demo


interface LoanApplicationHistory {
    fun saveLoanApplication(application: LoanTerms)
    fun getLoanApplication(): LoanTerms?
}