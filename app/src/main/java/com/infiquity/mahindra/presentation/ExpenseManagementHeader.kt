package com.infiquity.mahindra.presentation

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.infiquity.mahindra.R
import com.infiquity.mahindra.ui.dashboard.viewmodel.CartFilterOption
import com.infiquity.mahindra.ui.theme.Pink80

@Composable
fun ExpenseManagementHeader(
    selectedTab: CartFilterOption,
    dateRange: String,
    onTabSelected: (CartFilterOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        // Drawable Background
        Image(
            painter = painterResource(id = R.drawable.ic_bg_dashboard), // Your background drawable
            contentDescription = "Dashboard Background",
            modifier = Modifier.fillMaxWidth()
                .wrapContentSize()
                .background(color = Color(0xFF497FDC).copy(alpha = 0.8f)),
            contentScale = ContentScale.FillBounds // Ensures the image scales properly
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .padding(top = 40.dp)
        ) {
            // Row 1: Title and Info Icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.expense_management).uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Icon(
                    painter = painterResource(id = R.drawable.icon_info_settings), // Your info icon
                    contentDescription = "Info Icon",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Row 2: Tabs
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x1A000000)), // Transparent black background with rounded corners
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TabItem(
                        text = stringResource(id = R.string.title_weekly),
                        isSelected = selectedTab == CartFilterOption.WEEKLY,
                        onClick = { onTabSelected(CartFilterOption.WEEKLY) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TabItem(
                        text = stringResource(id = R.string.title_monthly),
                        isSelected = selectedTab == CartFilterOption.MONTHLY,
                        onClick = { onTabSelected(CartFilterOption.MONTHLY) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Row 3: Date Range
            Text(
                text = dateRange,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

        }
    }
}



@Composable
fun TabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Color.White else Color.Transparent) // Selected and unselected colors
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.Blue else Pink80,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}
@Preview(showBackground = true, name = "Expense Management Header Preview")
@Composable
fun PreviewExpenseManagementHeader() {
    ExpenseManagementHeader(
        selectedTab = CartFilterOption.WEEKLY, // Mock selected tab
        dateRange = "15 Feb 2024 - 21 Feb 2024", // Mock date range
        onTabSelected = { /* No-op for preview */ }
    )
}