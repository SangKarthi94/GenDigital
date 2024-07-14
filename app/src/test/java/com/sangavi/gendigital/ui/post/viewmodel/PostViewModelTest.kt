package com.sangavi.gendigital.ui.post.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.appmattus.kotlinfixture.kotlinFixture
import com.sangavi.gendigital.domain.core.Result
import com.sangavi.gendigital.domain.usecase.GetPostListUseCase
import com.sangavi.gendigital.domain.usecase.GetUserPostListUseCase
import com.sangavi.gendigital.ui.post.model.PostListData
import com.sangavi.gendigital.utils.MainCoroutineScopeRule
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class PostViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineScopeRule = MainCoroutineScopeRule()

    private val fixture = kotlinFixture()

    @Mock
    lateinit var mockGetListUseCase: GetPostListUseCase

    @Mock
    lateinit var mockGetPostListUseCase: GetUserPostListUseCase

    private fun createViewModel(): PostViewModel {
        return PostViewModel(
            getListUseCase = mockGetListUseCase,
            getPostListUseCase = mockGetPostListUseCase
        )
    }

    @org.junit.Test
    fun `Given UseCase returns Success result - When ViewModel is initialized - Then should emit success emit`() =
        runTest {
            // Given
            val userListResponse = fixture<List<PostListData>>()

            val result = Result.Success(userListResponse)
            whenever(mockGetListUseCase.invoke(Unit)).thenReturn(result)

            // When
            val viewModel = createViewModel()

            // Then
            val expectedResult = PostViewModel.PostViewState(
                postListData = userListResponse
            )
            Assert.assertThat(
                viewModel.postListStateFlow.value,
                CoreMatchers.equalTo(expectedResult)
            )
        }



    @org.junit.Test
    fun `Given UseCase returns Error result - When ViewModel is initialized - Then should emit error event`() =
        runTest {
            // Given
            val exception = Exception()
            val result = Result.Error(exception)
            whenever(mockGetListUseCase.invoke(Unit)).thenReturn(result)

            // When
            val viewModel = createViewModel()

            // Then
            val expectedResult = PostViewModel.PostViewState(
                error = "Unknown error"
            )
            Assert.assertThat(
                viewModel.postListStateFlow.value,
                CoreMatchers.equalTo(expectedResult)
            )
        }
}