package com.infiquity.mahindra.ui.dashboard

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.infiquity.mahindra.presentation.DashboardScreen
import com.infiquity.mahindra.ui.dashboard.viewmodel.DashboardViewModel
import com.infiquity.mahindra.ui.theme.MahindraTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Arrays


@AndroidEntryPoint
class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MahindraTheme {
                val viewModel = hiltViewModel<DashboardViewModel>()


//                    viewModel.getCartList(userDetail.id)
                    DashboardScreen(
                        onProfileClick = { navigateToProfile() },
                        onDismissErrorDialog = viewModel::dismissErrorDialog,
                        viewModel
                    )

            }
        }
    }

    private fun navigateToProfile() {
       Log.e("Navigation", "DashboardActivity")
    }

    fun longestCommonPrefix(arr: Array<String>): String {
        // Sort the array of strings

        Arrays.sort(arr)


        // Get the first and last strings after sorting
        val first = arr[0]
        val last = arr[arr.size - 1]
       /* val minLength = min(
            first.length.toDouble(),
            last.length.toDouble()
        ).toInt()*/

        val minLength = if (first.length < last.length) first.length else last.length


        // Find the common prefix between the first
        // and last strings
        var i = 0
        while (i < minLength &&
            first[i] == last[i]
        ) {
            i++
        }

        // Return the common prefix
        return first.substring(0, i)
    }

    fun main() {
        val arr = arrayOf(
            "geeksforgeeks", "geeks",
            "geek", "geezer"
        )
        println(longestCommonPrefix(arr))
    }
}