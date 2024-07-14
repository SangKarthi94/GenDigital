package com.sangavi.gendigital.ui.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangavi.gendigital.domain.core.Result
import com.sangavi.gendigital.domain.usecase.GetUserListUseCase
import com.sangavi.gendigital.ui.user.mapper.UserListUIMapper
import com.sangavi.gendigital.ui.user.model.UserListUIData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class UserLoginViewModel @Inject constructor(
    private val getUserListUseCase: GetUserListUseCase,
    private val userListUIMapper: UserListUIMapper
) : ViewModel() {

    private val _userListStateFlow = MutableStateFlow(UserViewState())
    val userListStateFlow = _userListStateFlow.asStateFlow()

    init {
        fetchUserList()
    }

    private fun fetchUserList() {
        viewModelScope.launch {
            _userListStateFlow.update { it.copy(isLoading = true) }

            try {
                when (val result = getUserListUseCase(Unit)) {
                    is Result.Error -> {
                        _userListStateFlow.update {
                            it.copy(error = result.exception.message ?: "Unknown error", isLoading = false)
                        }
                    }
                    is Result.Loading -> {
                        _userListStateFlow.update { it.copy(isLoading = true) }
                    }
                    is Result.Success -> {
                        val uiModels = result.data.map { userListUIMapper.map(it) }
                        _userListStateFlow.update {
                            it.copy(
                                userListData = uiModels,
                                isLoading = false
                            )
                        }
                    }
                }
            } catch (e: UnknownHostException) {
                _userListStateFlow.update {
                    it.copy(
                        error = "Network error: Unable to resolve host. Please check your internet connection.",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _userListStateFlow.update {
                    it.copy(
                        error = e.message ?: "An unexpected error occurred",
                        isLoading = false
                    )
                }
            }
        }
    }

    data class UserViewState(
        val userListData: List<UserListUIData> = emptyList(),
        val isLoading: Boolean = false,
        val error: String = "",
        val userDetail: UserListUIData? = null,
    )

    fun getUserDetail(userName: String) {
        val filteredValue = userListStateFlow.value.userListData.findLast { it.userName.equals(userName, true) }

        if (filteredValue != null) {
            _userListStateFlow.update {
                it.copy(userDetail = filteredValue)
            }
        } else {
            _userListStateFlow.update {
                it.copy(error = "No User Found")
            }
        }
    }

    fun dismissErrorDialog() {
        _userListStateFlow.update {
            it.copy(error = "")
        }
    }
}
