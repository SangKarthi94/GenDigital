package com.sangavi.gendigital.domain.usecase


import com.sangavi.gendigital.data.repository.DataRepository
import com.sangavi.gendigital.di.qualifier.IoDispatcher
import com.sangavi.gendigital.domain.core.UseCase
import com.sangavi.gendigital.ui.post.model.PostListData
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject


class GetUserPostListUseCase @Inject constructor(
    private val postRepository: DataRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Int, List<PostListData>>(dispatcher) {

    override suspend fun execute(userId: Int): List<PostListData> {
        return postRepository.getUserPostList(userId)
    }
}
