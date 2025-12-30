package com.nparmenov.wiam_demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity: ComponentActivity() {

    private val viewModel: LoanCalculatorViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                LoanCalculatorScreen(
                    viewModel = viewModel,
                    modifier = Modifier
                )
            }
        }
    }
}