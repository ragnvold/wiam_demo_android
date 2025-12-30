package com.nparmenov.wiam_demo

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class LoanApplicationRequest(
    val amount: Int,
    val period: Int,
    val totalRepayment: Int,
)

@Serializable
data class LoanApplicationResponse(
    val id: Int,
    val amount: Int,
    val period: Int,
    val totalRepayment: Int,
)

sealed interface ApiError {
    data class Http(val code: Int, val body: String?) : ApiError
    data object Network : ApiError
    data object Timeout : ApiError
    data class Unknown(val message: String?) : ApiError
}

sealed interface ApiResult<out T> {
    data class Ok<T>(val value: T) : ApiResult<T>
    data class Err(val error: ApiError) : ApiResult<Nothing>
}

class LoanApi(
    private val client: HttpClient,
    private val baseUrl: String,
) {

    suspend fun submitLoanApplication(req: LoanApplicationRequest): ApiResult<LoanApplicationResponse> {
        return try {
            val response = client.post("$baseUrl/posts") {
                contentType(io.ktor.http.ContentType.Application.Json)
                setBody(req)
            }
            if (response.status.isSuccess()) {
                val body = response.body<LoanApplicationResponse>()
                ApiResult.Ok(body)
            } else {
                val text = runCatching { response.bodyAsText() }.getOrNull()
                ApiResult.Err(ApiError.Http(response.status.value, text))
            }
        } catch (e: Exception) {
            ApiResult.Err(ApiError.Unknown(e.message))
        }
    }
}