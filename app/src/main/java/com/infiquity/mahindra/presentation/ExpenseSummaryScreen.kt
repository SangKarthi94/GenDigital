package com.infiquity.mahindra.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.infiquity.mahindra.R

@Composable
fun ExpenseSummary(totalExpenses: Double, energyConsumed: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFF497FDC).copy(alpha = 0.8f)), // Background color for the row
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        SummaryItem(
            icon = painterResource(id = R.drawable.ic_expenses),
            title = stringResource(id = R.string.total_expenses),
            value = "₹ ${totalExpenses.toInt()}",
            modifier = Modifier.weight(1f) // Equal width for each item
        )
        SummaryItem(
            icon = painterResource(id = R.drawable.ic_kwh),
            title = stringResource(id = R.string.energy_consumed),
            value = "${energyConsumed} kWh",
            modifier = Modifier.weight(1f) // Equal width for each item
        )
    }
}

@Composable
fun SummaryItem(icon: Painter, title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(10.dp), // Margin around the card
        colors = CardDefaults.cardColors(containerColor = Color.White), // Card background color
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Optional: add elevation for shadow
        shape = RoundedCornerShape(8.dp) // Optional: rounded corners
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth() // Makes content fill the Card width
                .padding(10.dp), // Padding inside the Card
            horizontalAlignment = Alignment.Start, // Center-align content horizontally
            verticalArrangement = Arrangement.Center // Center-align content vertically if needed
        ) {
            Icon(
                painter = icon, // Use an appropriate info icon
                contentDescription = "Info Icon",
                tint = Color.Blue,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style =  MaterialTheme.typography.bodyLarge.copy(lineHeight = 18.sp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 30.sp, color = Color.Blue)
            )
        }
    }
}
@Preview(showBackground = true, name = "Expense Summary Preview", widthDp = 360, heightDp = 200)
@Composable
fun PreviewExpenseSummary() {
    // Mock Data for Preview
    val totalExpenses = 714.0
    val energyConsumed = 119

    // Call the ExpenseSummary Composable
    ExpenseSummary(
        totalExpenses = totalExpenses,
        energyConsumed = energyConsumed
    )
}
