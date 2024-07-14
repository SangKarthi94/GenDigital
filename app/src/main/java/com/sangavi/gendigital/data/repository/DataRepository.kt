package com.sangavi.gendigital.data.repository

import com.sangavi.gendigital.data.RemoteDataSource
import com.sangavi.gendigital.model.UserListResponseModel
import com.sangavi.gendigital.ui.post.model.PostListData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun getUserList(): List<UserListResponseModel> {
        return remoteDataSource.getUserList()
    }

    suspend fun getPostList(): List<PostListData> {
        return remoteDataSource.getPostList()
    }

    suspend fun getUserPostList(userId: Int): List<PostListData> {
        return remoteDataSource.getUserPostList(userId)
    }
}
