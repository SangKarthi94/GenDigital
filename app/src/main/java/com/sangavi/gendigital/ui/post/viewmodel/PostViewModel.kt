package com.sangavi.gendigital.ui.post.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangavi.gendigital.domain.core.Result
import com.sangavi.gendigital.domain.usecase.GetPostListUseCase
import com.sangavi.gendigital.domain.usecase.GetUserPostListUseCase
import com.sangavi.gendigital.ui.post.model.PostListData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PostViewModel @Inject constructor(
    private val getListUseCase: GetPostListUseCase,
    private val getPostListUseCase: GetUserPostListUseCase,
) : ViewModel() {

    private val _postListStateFlow = MutableStateFlow(PostViewState())
    val postListStateFlow = _postListStateFlow.asStateFlow()

    private val _selectedFilter = MutableStateFlow(FilterOption.ALL_POSTS)
    val selectedFilter: StateFlow<FilterOption> = _selectedFilter


    init {
        getAllPostList()
    }

    private fun getAllPostList(){
        viewModelScope.launch {

            when (val result = getListUseCase(Unit)){
                is Result.Error -> {
                    _postListStateFlow.update {
                        it.copy(error = it.error)
                    }
                }
                is Result.Loading -> {
                    _postListStateFlow.update {
                        it.copy(isLoading = true)
                    }
                }
                is Result.Success -> {

                    Log.e("All post", "${result.data.size}")
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

    fun getUserPostList(userId: Int){
        viewModelScope.launch {

            when (val result = getPostListUseCase(userId)){
                is Result.Error -> {
                    _postListStateFlow.update {
                        it.copy(error = it.error)
                    }
                }
                is Result.Loading -> {
                    _postListStateFlow.update {
                        it.copy(isLoading = true)
                    }
                }
                is Result.Success -> {

                    Log.e("User post", "${result.data.size}")
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

    data class PostViewState(
        val postListData: List<PostListData> = emptyList(),
        val isLoading: Boolean = false,
        val error: String = "",
        val userPostListData: List<PostListData> = emptyList(),
    )

    fun setFilter(filter: FilterOption) {
        _selectedFilter.value = filter
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