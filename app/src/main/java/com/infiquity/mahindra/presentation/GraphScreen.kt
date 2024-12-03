package com.infiquity.mahindra.presentation

import android.graphics.Color
import android.widget.LinearLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.infiquity.mahindra.R
import com.infiquity.mahindra.ui.dashboard.model.Cart

@Composable
fun CombinedExpenseGraph(data: List<Cart>) {
    val barData = data.map { it.discountedTotal.toFloat() }
    val lineData = data.map { it.totalProducts.toFloat() }
    val userIds = data.map { it.userId.toString() }

    CombinedChartWithDualYAxis(
        barData = barData,
        lineData = lineData,
        labels = userIds,
        barLabel = stringResource(id = R.string.discounted_total),
        lineLabel = stringResource(id = R.string.total_products)
    )
}

@Composable
fun CombinedChartWithDualYAxis(
    barData: List<Float>,
    lineData: List<Float>,
    labels: List<String>,
    barLabel: String,
    lineLabel: String
) {
    AndroidView(
        factory = { context ->
            com.github.mikephil.charting.charts.CombinedChart(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    500 // Set a fixed height for the chart
                )
                description.isEnabled = false
                setPinchZoom(true)

                // Configure X-axis
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    valueFormatter = IndexAxisValueFormatter(labels)
                    setDrawGridLines(false)
                }

                // Configure left Y-axis
                axisLeft.apply {
                    setDrawGridLines(true)
                    granularity = 1f
                    axisMinimum = 0f // Start Y-axis from zero
                    textColor = Color.BLUE // Left Y-axis text color
                }

                // Configure right Y-axis
                axisRight.apply {
                    setDrawGridLines(false)
                    granularity = 1f
                    axisMinimum = 0f // Start Y-axis from zero
                    textColor = Color.BLUE // Right Y-axis text color
                }

                legend.apply {
                    isEnabled = true
                    verticalAlignment = Legend.LegendVerticalAlignment.TOP
                    horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                    orientation = Legend.LegendOrientation.HORIZONTAL
                    textSize = 12f
                    textColor = Color.BLACK
                }
            }
        },
        update = { chart ->
            val barEntries = barData.mapIndexed { index, value ->
                BarEntry(index.toFloat(), value)
            }
            val lineEntries = lineData.mapIndexed { index, value ->
                Entry(index.toFloat(), value)
            }

            val barDataSet = BarDataSet(barEntries, barLabel).apply {
                color = 0xFF0000FF.toInt() // Blue
                valueTextColor = 0xFF000000.toInt() // Black
                valueTextSize = 12f
                setDrawValues(false) // Hides value labels for lines
                axisDependency = YAxis.AxisDependency.LEFT
            }

            val lineDataSet = LineDataSet(lineEntries, lineLabel).apply {
                color = Color.GREEN
                lineWidth = 2f
                circleRadius = 4f
                setCircleColor(Color.GREEN)
                valueTextColor = Color.BLACK
                valueTextSize = 12f
                setDrawValues(false) // Hides value labels for lines
                axisDependency = YAxis.AxisDependency.RIGHT
            }

            val combinedData = CombinedData().apply {
                setData(BarData(barDataSet))
                setData(LineData(lineDataSet))
            }

            chart.data = combinedData
            chart.invalidate() // Refresh chart
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .padding(start = 5.dp, end = 5.dp)

    )
}
fun mockCartData(): List<Cart> {
    return listOf(
        Cart(userId = 1, discountedTotal = 1200.0, totalProducts = 5),
        Cart(userId = 2, discountedTotal = 800.0, totalProducts = 8),
        Cart(userId = 3, discountedTotal = 1500.0, totalProducts = 10),
        Cart(userId = 4, discountedTotal = 950.0, totalProducts = 3),
    )
}

// Preview for the Combined Chart
@Preview(showBackground = true, name = "Combined Expense Graph")
@Composable
fun CombinedExpenseGraphPreview() {
    CombinedExpenseGraph(data = mockCartData())
}




