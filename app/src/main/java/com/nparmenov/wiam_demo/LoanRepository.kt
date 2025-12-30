package com.nparmenov.wiam_demo

interface LoanRepository {
    suspend fun submitApplication(req: LoanApplicationRequest): LoanApplicationResponse
}