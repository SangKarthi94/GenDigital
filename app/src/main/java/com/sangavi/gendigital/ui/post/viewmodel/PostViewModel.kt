package com.sangavi.gendigital.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangavi.gendigital.domain.core.Result
import com.sangavi.gendigital.domain.usecase.GetPostListUseCase
import com.sangavi.gendigital.domain.usecase.GetUserPostListUseCase
import com.sangavi.gendigital.ui.post.model.PostListData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.net.UnknownHostException
import javax.inject.Inject


@HiltViewModel
class PostViewModel @Inject constructor(
    private val getListUseCase: GetPostListUseCase,
    private val getPostListUseCase: GetUserPostListUseCase,
) : ViewModel() {

    private val _postListStateFlow = MutableStateFlow(PostViewState())
    val postListStateFlow: StateFlow<PostViewState> = _postListStateFlow

    private val _selectedFilter = MutableStateFlow(FilterOption.ALL_POSTS)
    val selectedFilter: StateFlow<FilterOption> = _selectedFilter


    init {
        getAllPostList()
    }

    private fun getAllPostList() {
        viewModelScope.launch {
            _postListStateFlow.update { it.copy(isLoading = true) }

            val result = withTimeoutOrNull(30_000) {
                try {
                    getListUseCase(Unit)
                } catch (e: UnknownHostException) {
                    Result.Error(e)
                } catch (e: Exception) {
                    Result.Error(e)
                }
            }

            if (result == null) {
                /*Handle timeout error (If it occurs please Invalidate and restart android studio,
                Its happening time to time due to URL issue)*/
                _postListStateFlow.update {
                    it.copy(isLoading = false, error = "Request timed out. Please try again.")
                }
            } else {
                when (result) {
                    is Result.Error -> {
                        _postListStateFlow.update {
                            val errorMessage = if (result.exception is UnknownHostException) {
                                "Network error: Unable to resolve host. Please check your internet connection."
                            } else {
                                result.exception.message ?: "Unknown error"
                            }
                            it.copy(error = errorMessage, isLoading = false)
                        }
                    }

                    is Result.Loading -> {
                        _postListStateFlow.update { it.copy(isLoading = true) }
                    }

                    is Result.Success -> {
                        _postListStateFlow.update {
                            it.copy(
                                postListData = result.data,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun getUserPostList(userId: Int) {
        viewModelScope.launch {
            _postListStateFlow.update { it.copy(isLoading = true) }

            val result = withTimeoutOrNull(30_000) {
                try {
                    getPostListUseCase(userId)
                } catch (e: UnknownHostException) {
                    Result.Error(e)
                } catch (e: Exception) {
                    Result.Error(e)
                }
            }

            if (result == null) {
                // Handle timeout error
                _postListStateFlow.update {
                    it.copy(isLoading = false, error = "Request timed out. Please try again.")
                }
            } else {
                when (result) {
                    is Result.Error -> {
                        _postListStateFlow.update {
                            val errorMessage = if (result.exception is UnknownHostException) {
                                "Network error: Unable to resolve host. Please check your internet connection."
                            } else {
                                result.exception.message ?: "Unknown error"
                            }
                            it.copy(error = errorMessage, isLoading = false)
                        }
                    }

                    is Result.Loading -> {
                        _postListStateFlow.update { it.copy(isLoading = true) }
                    }

                    is Result.Success -> {
                        _postListStateFlow.update {
                            it.copy(
                                userPostListData = result.data,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    data class PostViewState(
        val postListData: List<PostListData> = emptyList(),
        val isLoading: Boolean = false,
        val error: String = "",
        val userPostListData: List<PostListData> = emptyList(),
    )

    fun setFilter(filter: FilterOption) {
        _selectedFilter.value = filter
    }

    fun getFilter(): FilterOption {
        return _selectedFilter.value
    }

    fun dismissErrorDialog() {
        _postListStateFlow.update {
            it.copy(error = "")
        }
    }

}

enum class FilterOption {
    ALL_POSTS,
    MY_POSTS
}