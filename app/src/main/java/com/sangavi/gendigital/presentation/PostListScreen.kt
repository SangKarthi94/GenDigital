package com.sangavi.gendigital.presentation

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sangavi.gendigital.ui.post.model.PostListData
import com.sangavi.gendigital.ui.post.viewmodel.FilterOption
import com.sangavi.gendigital.ui.post.viewmodel.PostViewModel
import com.sangavi.gendigital.ui.user.model.UserListUIData
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListUIScreen(
    userListUIData: UserListUIData,
    onProfileClick: () -> Unit,
    viewModel: PostViewModel
) {
//    val context = LocalContext.current
    val showBottomSheet = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val modalBottomSheetState = rememberModalBottomSheetState(false)

    // Collect the state flow from the view model
    val postListState by viewModel.postListStateFlow.collectAsState()

    // Local state to hold the filtered list of posts
    val postList = remember { mutableStateOf<List<PostListData>>(emptyList()) }
    val filter by viewModel.selectedFilter.collectAsState()  // Collect the selected filter state

    // Update the local list based on filter and postListState changes
    LaunchedEffect(postListState) {
        postList.value = when (viewModel.getFilter()) {
            FilterOption.ALL_POSTS -> postListState.postListData
            FilterOption.MY_POSTS -> postListState.userPostListData
        }
    }

    // Handle filter changes
    LaunchedEffect(viewModel.getFilter()) {
        postList.value = when (viewModel.getFilter()) {
            FilterOption.ALL_POSTS -> postListState.postListData
            FilterOption.MY_POSTS -> postListState.userPostListData
        }
    }


    //Bottom sheet integration
    if (showBottomSheet.value) {
        FilterBottomSheet(
            initialFilter = viewModel.selectedFilter.collectAsState().value,
            onFilterSelected = { selectedFilter ->
                viewModel.setFilter(selectedFilter)
            },
            onDismiss = {
                coroutineScope.launch {
                    modalBottomSheetState.hide()
                }
                showBottomSheet.value = false

                // Update postList based on the new filter
                postList.value = when (viewModel.getFilter()) {
                    FilterOption.ALL_POSTS -> postListState.postListData
                    FilterOption.MY_POSTS -> postListState.userPostListData
                }

            },
            modalBottomSheetState = modalBottomSheetState
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = when (filter) {
                    FilterOption.ALL_POSTS -> "All Posts"
                    FilterOption.MY_POSTS -> "My Posts"
                }) },
                actions = {
                    ProfileRow(
                        userListUIData.name,
                        onClick = {
                            // Handle the click event for the profile image and name
                            onProfileClick()
                        }
                    )
                }
            )
        },
        content = {
            //Main Content Page
            Column(modifier = Modifier.padding(it)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Filter",
                        modifier = Modifier.clickable {
                            showBottomSheet.value = true
                        }.padding(end = 25.dp)
                    )
                }
                when {
                    postListState.isLoading -> {
                        // Display loading indicator
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    postListState.error.isNotEmpty() -> {
                        // Display error message
                        Text(
                            text = postListState.error,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    else -> {
                        // RecyclerView for displaying post list data
                        PostListRecyclerView(postList.value)
                    }
                }
            }
        }
    )

}

@Composable
fun PostListRecyclerView(postList: List<PostListData>) {
    LazyColumn {
        itemsIndexed(postList) { index, news ->
            PostListItem(index, news)
        }
    }
}

@Composable
fun PostListItem(index: Int, post: PostListData) {
    Log.e("Index", "$index")
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = post.title, style = MaterialTheme.typography.titleLarge)
        Text(text = post.body, style = MaterialTheme.typography.bodyMedium)
    }
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