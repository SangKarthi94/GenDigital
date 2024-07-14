package com.sangavi.gendigital.data.remote.service


import com.sangavi.gendigital.model.UserListResponseModel
import com.sangavi.gendigital.ui.post.model.PostListData
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("users")
    suspend fun getUserListData(): List<UserListResponseModel>

    @GET("posts")
    suspend fun getPostListData(): List<PostListData>

    @GET("posts?")
    suspend fun getUserPostListData(@Query("userId") userId: Int): List<PostListData>
}
