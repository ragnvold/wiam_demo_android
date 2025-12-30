package com.nparmenov.wiam_demo

import android.content.Context
import android.content.SharedPreferences
import kotlinx.serialization.json.Json
import androidx.core.content.edit

class LoanApplicationHistoryImpl(
    context: Context,
    private val json: Json
): LoanApplicationHistory {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

    private companion object {
        private const val PREFS_NAME = "loan_history"
        private const val KEY_LAST_LOAN_APPLICATION = "last_loan_application"
    }

    override fun saveLoanApplication(
        application: LoanTerms
    ) {
        val jsonString = json.encodeToString(application)
        prefs.edit {
            putString(KEY_LAST_LOAN_APPLICATION, jsonString)
        }
    }

    override fun getLoanApplication(): LoanTerms? {
        val jsonString = prefs.getString(KEY_LAST_LOAN_APPLICATION, null)
            ?: return null

        return json.decodeFromString<LoanTerms>(jsonString)
    }
}