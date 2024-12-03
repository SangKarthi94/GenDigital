package com.infiquity.mahindra.data.remote.service


import com.infiquity.mahindra.ui.dashboard.model.CartListData
import retrofit2.http.GET

interface APIService {
    @GET("carts")
    suspend fun getCartListData(): CartListData
}
