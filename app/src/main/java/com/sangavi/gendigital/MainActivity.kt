package com.sangavi.gendigital

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sangavi.gendigital.data.SharedPrefManager
import com.sangavi.gendigital.presentation.UserLoginScreen
import com.sangavi.gendigital.ui.post.PostActivity
import com.sangavi.gendigital.ui.theme.GenDigitalTheme
import com.sangavi.gendigital.ui.user.viewmodel.UserLoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //SharedPrefManager.clearUserListUIData(this) -> to clear
        setContent {
            GenDigitalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {

                    val viewModel = hiltViewModel<UserLoginViewModel>()

                    val state by viewModel.userListStateFlow.collectAsState()

                    /*val intent = Intent(this, PostActivity::class.java)
                    startActivity(intent)*/

                    if (state.userDetail != null) {
                        // Save the data
                        SharedPrefManager.saveUserListUIData(this, state.userDetail!!)
                        // Navigate to another activity if User found
                        val intent = Intent(this, PostActivity::class.java)
                        startActivity(intent)
                    }

                    UserLoginScreen(
                        userListState = state,
                        onLoginClicked = viewModel::getUserDetail,
                        onDismissErrorDialog = viewModel::dismissErrorDialog,
                        Modifier
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color.White)
                            .padding(top = 0.dp, start = 40.dp, bottom = 0.dp, end = 40.dp)
                            .padding(it)
                    )

                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UserLoginScreen(
        userListState = null,
        onLoginClicked = {},
        onDismissErrorDialog = {},
        Modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(5.dp))
            .background(Color.White)
            .padding(top = 0.dp, start = 40.dp, bottom = 0.dp, end = 40.dp)
    )
}