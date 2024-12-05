package com.infiquity.mahindra.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.infiquity.mahindra.R
import com.infiquity.mahindra.ui.dashboard.model.Cart
import com.infiquity.mahindra.ui.dashboard.viewmodel.CartFilterOption
import com.infiquity.mahindra.ui.dashboard.viewmodel.DashboardViewModel
import com.infiquity.mahindra.ui.theme.Pink80


@Composable
fun DashboardScreen(
    onProfileClick: () -> Unit,
    onDismissErrorDialog: () -> Unit,
    viewModel: DashboardViewModel
) {

    ExpenseManagementScreen(viewModel = viewModel)
}

@Composable
fun ExpenseManagementScreen(viewModel: DashboardViewModel) {
    val viewState by viewModel.cartListStateFlow.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    // Determine date range based on selected filter
    val dateRange = when (selectedFilter) {
        CartFilterOption.WEEKLY -> viewModel.getLast7DaysDateRange(true) //will pick data from last 7 dates
        CartFilterOption.MONTHLY -> viewModel.getLast7DaysDateRange(false) //will pick data from last 30 dates
    }

    Scaffold(
        topBar = {
            ExpenseManagementHeader(
                selectedTab = selectedFilter,
                dateRange = dateRange,
                onTabSelected = { filter ->
                    viewModel.updateFilter(filter)
                }
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                if (viewState.isLoading) {
                    val infiniteTransition = rememberInfiniteTransition(label = "")
                    val progressAnimate by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 1000, easing = LinearEasing)
                        ), label = ""
                    )
                    CircularProgressIndicator( // ******* Have to make it center aligner
                        progress = progressAnimate,
//                        modifier = Modifier.align(Alignment.CenterVertically),
                        strokeCap = StrokeCap.Round,
                        color = Color.Gray
                    )
                } else if (viewState.error.isNotEmpty()) {
                    showErrorDialog(
                        errorMessage = viewState.error,
                        onDismiss = { viewModel.dismissErrorDialog() },
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color.White)
                            .padding(16.dp)
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()) // Makes it scrollable
                    ) {
                        ExpenseContent(viewState)
                    }
                }
            }
        }
    )
}

@Composable
fun ExpenseContent(viewState: DashboardViewModel.CartViewState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Summary Section
        ExpenseSummary(
            totalExpenses = viewState.totalExpenses,
            energyConsumed = viewState.energyConsumed
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.expense_title),
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color.Black,
                fontSize = 18.sp
            ),
            modifier = Modifier.padding(start = 10.dp),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Graphs
        if (viewState.filteredCartListData.isNotEmpty()) {

            CombinedExpenseGraph(viewState.filteredCartListData)

        } else {
            Text(
                text = "No data available",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(CenterHorizontally)
            )
        }
    }
}

@Preview(showBackground = true, name = "Expense Management Screen Preview")
@Composable
fun PreviewExpenseManagementScreen() {
    // Mock ViewModel state
    val mockCartListState = DashboardViewModel.CartViewState(
        isLoading = false,
        error = "",
        totalExpenses = 1200.50,
        energyConsumed = 65,
        filteredCartListData = listOf(
            Cart(userId = 1, discountedTotal = 300.0, totalProducts = 5),
            Cart(userId = 2, discountedTotal = 400.0, totalProducts = 6),
            Cart(userId = 3, discountedTotal = 500.0, totalProducts = 7)
        )
    )

    val mockSelectedFilter = CartFilterOption.WEEKLY

    ExpenseManagementScreenPreviewContent(
        viewState = mockCartListState,
        selectedFilter = mockSelectedFilter,
        onTabSelected = {},
        onDismissErrorDialog = {}
    )
}

@Composable
fun ExpenseManagementScreenPreviewContent(
    viewState: DashboardViewModel.CartViewState,
    selectedFilter: CartFilterOption,
    onTabSelected: (CartFilterOption) -> Unit,
    onDismissErrorDialog: () -> Unit
) {
    val dateRange = when (selectedFilter) {
        CartFilterOption.WEEKLY -> "15 Feb 2024 - 21 Feb 2024"
        CartFilterOption.MONTHLY -> "01 Feb 2024 - 28 Feb 2024"
    }

    Scaffold(
        topBar = {
            ExpenseManagementHeader(
                selectedTab = selectedFilter,
                dateRange = dateRange,
                onTabSelected = onTabSelected
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (viewState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        strokeCap = StrokeCap.Round,
                        color = Color.Gray
                    )
                } else if (viewState.error.isNotEmpty()) {
                    showErrorDialog(
                        errorMessage = viewState.error,
                        onDismiss = onDismissErrorDialog,
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color.White)
                            .padding(16.dp)
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()) // Makes it scrollable
                    ) {
                        ExpenseContent(viewState)
                    }
                }
            }
        }
    )
}




