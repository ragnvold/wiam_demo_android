package com.nparmenov.wiam_demo

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.math.BigDecimal
import java.util.Calendar

fun provideLoanRepository(api: LoanApi): LoanRepository = LoanRepositoryImpl(api)

fun provideLoanApplicationHistory(context: Context, json: Json): LoanApplicationHistory =
    LoanApplicationHistoryImpl(context, json)

fun provideHttpClient(): HttpClient =
    HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json()
        }

        engine {
            config {
                retryOnConnectionFailure(true)
            }
        }
    }

val appModule = module {

    single(named("baseUrl")) { "https://jsonplaceholder.typicode.com" }

    single { Calendar.getInstance() }

    single { LoanCalculator() }

    single { CoroutineScope(Dispatchers.IO) }

    single { provideHttpClient() }

    single { LoanApi(client = get(), baseUrl = get(named("baseUrl"))) }

    single { provideLoanRepository(get()) }

    single { provideLoanApplicationHistory(androidContext(), get()) }

    single {
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            explicitNulls = false
        }
    }

    single {
        val res = androidContext().resources
        LoanCalculatorConfig(
            minAmountUsd = BigDecimal(res.getInteger(R.integer.loan_min_amount_usd)),
            maxAmountUsd = BigDecimal(res.getInteger(R.integer.loan_max_amount_usd)),
            amountStepUsd = BigDecimal(res.getInteger(R.integer.loan_amount_step_usd)),
            allowedTermsDays = res.getIntArray(R.array.loan_allowed_terms_days).toSet()
        )
    }

    single { LoanApplicationValidator(get()) }

    single {
        val aprPercent = BigDecimal(androidContext().resources.getInteger(R.integer.loan_apr_percent))
        val basePeriodDays = androidContext().resources.getIntArray(R.array.loan_allowed_terms_days)[1]
        LoanComputationMiddleware(
            calendar = get(),
            loanCalculator = get(),
            aprPercent = aprPercent,
            basePeriodDays = basePeriodDays
        )
    }

    single { SaveLastLoanApplicationMiddleware(get()) }

    single { RestoreLastLoanApplicationMiddleware(get()) }

    single { SubmitLoanApplicationMiddleware(repository = get(), validator = get(), scope = get()) }

    viewModel {
        LoanCalculatorViewModel(
            restoreLastLoanApplicationMiddleware = get(),
            saveLastLoanApplicationMiddleware = get(),
            loanComputationMiddleware = get(),
            submitLoanApplicationMiddleware = get(),
            validator = get(),
            config = get()
        )
    }
}
