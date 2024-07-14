package com.sangavi.gendigital.ui.post

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.hilt.navigation.compose.hiltViewModel
import com.sangavi.gendigital.R
import com.sangavi.gendigital.data.SharedPrefManager
import com.sangavi.gendigital.presentation.postListUIScreen
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
        Log.e("Stored Data", "Name ${userDetail!!.name}")

        findViewById<ComposeView>(R.id.compose_view).setContent {
            GenDigitalTheme {
                val viewModel = hiltViewModel<PostViewModel>()

                if (userDetail != null) {
                    viewModel.getUserPostList(userDetail.id)
                    postListUIScreen(userDetail, onProfileClick = { navigateToProfile() }, viewModel)
                }
            }
        }
    }

    private fun navigateToProfile() {

        Log.d("FragmentNavigation", "Navigating to ProfileFragment")

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, ProfileFragment())
            .addToBackStack("")
            .commit()

        val fragmentCount = supportFragmentManager.fragments.size
        Log.d("FragmentNavigation", "Number of fragments: $fragmentCount")
    }

}

