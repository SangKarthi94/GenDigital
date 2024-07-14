package com.sangavi.gendigital.data

import com.sangavi.gendigital.data.remote.service.APIService
import com.sangavi.gendigital.model.UserListResponseModel
import com.sangavi.gendigital.ui.post.model.PostListData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val newsService: APIService
) {
    suspend fun getUserList(): List<UserListResponseModel> {
        return newsService.getUserListData()
    }

    suspend fun getPostList(): List<PostListData> {
        return newsService.getPostListData()
    }

    suspend fun getUserPostList(userId: Int): List<PostListData> {
        return newsService.getUserPostListData(userId)
    }
}
