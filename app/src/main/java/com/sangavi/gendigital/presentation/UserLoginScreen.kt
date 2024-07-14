package com.sangavi.gendigital.presentation

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangavi.gendigital.R
import com.sangavi.gendigital.ui.user.viewmodel.UserLoginViewModel


@Composable
fun UserLoginScreen(
    userListState: UserLoginViewModel.UserViewState?,
    onLoginClicked: (userName: String) -> Unit,
    onDismissErrorDialog: () -> Unit,
    modifier: Modifier
) {

    Box(modifier = Modifier.fillMaxSize()) {

        userLoginDesign(
            onLoginClicked = {
                Log.e("User State", "Name $it")
                onLoginClicked(it)
            }, modifier
        )

        userListState?.let {
            if (it.isLoading) {
                Log.e("User State", "Loading")
                val infiniteTransition = rememberInfiniteTransition(label = "")
                val progressAnimate by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 1000, easing = LinearEasing)
                    ), label = ""
                )
                CircularProgressIndicator(
                    progress = progressAnimate,
                    modifier = Modifier.align(Alignment.Center),
                    strokeCap = StrokeCap.Round,
                    color = Color.Gray
                )
            } else if (it.error.isNotEmpty()) {
                Log.e("User State", "Error")
                showErrorDialog(
                    errorMessage = it.error,
                    onDismiss = { onDismissErrorDialog() },
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.White)
                        .padding(16.dp)
                )
            }
        }
    }

}


@Composable
fun userLoginDesign(
    onLoginClicked: (userName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val userName = remember { mutableStateOf("") }

    // Determine screen orientation
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    /*if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        // Our UI for portrait orientation
    } else {
        // Our UI for landscape orientation
    }*/

    // Use Box to manage alignment of content
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = if (isLandscape) 32.dp else 16.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Hello,\nWelcome to the login page",
                fontSize = 25.sp,
                color = Color.Blue,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = userName.value,
                onValueChange = { userName.value = it },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "person") },
                label = { Text(text = "username") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                )
            )

            Spacer(modifier = Modifier.height(36.dp))

            OutlinedButton(
                onClick = {
                    Log.e("User State", "Button Clicked ${userName.value}")
                    onLoginClicked(userName.value)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Login",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun MyViewPreview() {
    userLoginDesign(
        onLoginClicked = {}, Modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(5.dp))
            .background(Color.White)
            .padding(top = 0.dp, start = 40.dp, bottom = 0.dp, end = 40.dp)
    )
}