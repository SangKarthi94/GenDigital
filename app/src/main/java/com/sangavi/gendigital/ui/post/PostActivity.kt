package com.sangavi.gendigital.ui.post

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.hilt.navigation.compose.hiltViewModel
import com.sangavi.gendigital.R
import com.sangavi.gendigital.data.SharedPrefManager
import com.sangavi.gendigital.presentation.PostListUIScreen
import com.sangavi.gendigital.ui.post.profile.ProfileFragment
import com.sangavi.gendigital.ui.post.viewmodel.PostViewModel
import com.sangavi.gendigital.ui.theme.GenDigitalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        enableEdgeToEdge()

        val userDetail = SharedPrefManager.getUserListUIData(this)

        findViewById<ComposeView>(R.id.compose_view).setContent {
            GenDigitalTheme {
                val viewModel = hiltViewModel<PostViewModel>()

                if (userDetail != null) {
                    viewModel.getUserPostList(userDetail.id)
                    PostListUIScreen(
                        userDetail,
                        onProfileClick = { navigateToProfile() },
                        onDismissErrorDialog = viewModel::dismissErrorDialog,
                        viewModel
                    )
                }
            }
        }
    }

    private fun navigateToProfile() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, ProfileFragment())
            .addToBackStack("")
            .commit()
    }

}

