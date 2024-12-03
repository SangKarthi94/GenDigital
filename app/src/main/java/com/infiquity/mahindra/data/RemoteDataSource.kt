package com.infiquity.mahindra.data

import com.infiquity.mahindra.data.remote.service.APIService
import com.infiquity.mahindra.ui.dashboard.model.CartListData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val cartService: APIService
) {
    suspend fun getCartListData(): CartListData {
        return cartService.getCartListData()
    }
}
