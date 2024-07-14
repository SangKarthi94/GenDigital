package com.sangavi.gendigital.presentation

import FilterBottomSheet
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangavi.gendigital.ui.post.viewmodel.FilterOption
import com.sangavi.gendigital.ui.post.viewmodel.PostViewModel
import com.sangavi.gendigital.ui.user.model.UserListUIData
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun postListUIScreen(
    userListUIData: UserListUIData,
    onProfileClick: () -> Unit,
    viewModel: PostViewModel
) {
    val context = LocalContext.current
    val showBottomSheet = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val modalBottomSheetState = rememberModalBottomSheetState(false)

    if (showBottomSheet.value) {
        FilterBottomSheet(
            viewModel = viewModel,
            onDismiss = {
                coroutineScope.launch {
                    modalBottomSheetState.hide()
                }
                showBottomSheet.value = false
            },
            modalBottomSheetState = modalBottomSheetState
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /*Text("Main Screen")*/ },
                actions = {
                    ProfileRow(
                        userListUIData.name,
                        onClick = {
                            // Handle the click event, e.g., show a toast or navigate to another screen
                            onProfileClick()
                            Log.e("Profile Clicked", "OnClick")
                        }
                    )
                }
            )
        },
        content = {

            Column(modifier = Modifier.padding(it)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Post List",
                        fontSize = 30.sp
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Filter",
                        modifier = Modifier.clickable {
                            showBottomSheet.value = true
                        }
                    )
                }

                // Display filtered content based on the selected filter
                when (viewModel.selectedFilter.collectAsState().value) {
                    FilterOption.ALL_POSTS -> {
                        // Display all posts
                        Toast.makeText(context,"All Post", Toast.LENGTH_SHORT).show()
                    }

                    FilterOption.MY_POSTS -> {
                        // Display user's posts
                        Toast.makeText(context,"My Post", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    )

}

//To make Profile Image onClick
@Composable
fun ProfileRow(
    userName: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = userName,
            modifier = Modifier.padding(end = 8.dp)
        )
        Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
    }
}