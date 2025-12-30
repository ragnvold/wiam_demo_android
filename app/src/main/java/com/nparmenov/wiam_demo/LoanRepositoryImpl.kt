package com.nparmenov.wiam_demo

class LoanRepositoryImpl(private val api: LoanApi): LoanRepository {

    override suspend fun submitApplication(req: LoanApplicationRequest): LoanApplicationResponse {
        val apiResult = api.submitLoanApplication(req)

        return when (apiResult) {
            is ApiResult.Ok -> apiResult.value
            is ApiResult.Err -> throw Exception(apiResult.error.toString())
        }
    }
}