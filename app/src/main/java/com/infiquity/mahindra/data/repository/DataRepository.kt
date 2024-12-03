package com.infiquity.mahindra.data.repository

import com.infiquity.mahindra.data.RemoteDataSource
import com.infiquity.mahindra.ui.dashboard.model.CartListData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun getCartListData(): CartListData {
        return remoteDataSource.getCartListData()
    }
}
