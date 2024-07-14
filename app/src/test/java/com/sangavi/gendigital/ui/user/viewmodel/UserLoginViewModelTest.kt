package com.sangavi.gendigital.ui.user.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.runtime.collectAsState
import com.appmattus.kotlinfixture.kotlinFixture
import com.sangavi.gendigital.domain.usecase.GetUserListUseCase
import com.sangavi.gendigital.ui.user.mapper.UserListUIMapper
import com.sangavi.gendigital.ui.user.model.UserListUIData
import com.sangavi.gendigital.utils.MainCoroutineScopeRule
import com.sangavi.gendigital.domain.core.Result
import com.sangavi.gendigital.model.UserListResponseModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Rule
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class UserLoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineScopeRule = MainCoroutineScopeRule()

    private val fixture = kotlinFixture()

    @Mock
    lateinit var mockGetUserListUseCase: GetUserListUseCase

    @Mock
    lateinit var mockUserListUIMapper: UserListUIMapper

    private fun createViewModel(): UserLoginViewModel {
        return UserLoginViewModel(
            getUserListUseCase = mockGetUserListUseCase,
            userListUIMapper = mockUserListUIMapper
        )
    }

    @org.junit.Test
    fun `Given UseCase returns Success result - When ViewModel is initialized - Then should emit success emit`() =
        runTest {
            // Given
            val userListResponse = fixture<List<UserListResponseModel>>()

            val result = Result.Success(userListResponse)
            whenever(mockGetUserListUseCase.invoke(Unit)).thenReturn(result)

            val newsArticleUIModel = fixture<UserListUIData>()
            whenever(mockUserListUIMapper.map(any())).thenReturn(newsArticleUIModel)

            // When
            val viewModel = createViewModel()

            // Then
            val expectedResult = UserLoginViewModel.UserViewState(
                userListData = userListResponse.map { newsArticleUIModel }
            )
            Assert.assertThat(
                viewModel.userListStateFlow.value,
                CoreMatchers.equalTo(expectedResult)
            )
        }

    @org.junit.Test
    fun `Given UseCase returns Error result - When ViewModel is initialized - Then should emit error event`() =
        runTest {
            // Given
            val exception = Exception()
            val result = Result.Error(exception)
            whenever(mockGetUserListUseCase.invoke(Unit)).thenReturn(result)

            // When
            val viewModel = createViewModel()

            // Then
            val expectedResult = UserLoginViewModel.UserViewState(
                error = "Unknown error"
            )
            Assert.assertThat(
                viewModel.userListStateFlow.value,
                CoreMatchers.equalTo(expectedResult)
            )
        }
}

