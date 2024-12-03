package com.infiquity.mahindra.ui.dashboard

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.infiquity.mahindra.presentation.DashboardScreen
import com.infiquity.mahindra.ui.dashboard.viewmodel.DashboardViewModel
import com.infiquity.mahindra.ui.theme.MahindraTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MahindraTheme {
                val viewModel = hiltViewModel<DashboardViewModel>()


//                    viewModel.getCartList(userDetail.id)
                    DashboardScreen(
                        onProfileClick = { navigateToProfile() },
                        onDismissErrorDialog = viewModel::dismissErrorDialog,
                        viewModel
                    )

            }
        }
    }

    private fun navigateToProfile() {
       Log.e("Navigation", "DashboardActivity")
    }
}