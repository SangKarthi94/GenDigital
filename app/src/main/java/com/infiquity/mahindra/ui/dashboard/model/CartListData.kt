package com.infiquity.mahindra.ui.dashboard.model

import java.time.LocalDate

data class CartListData(
    val carts: List<Cart>,
    val limit: Int,
    val skip: Int,
    val total: Int
)

data class Cart(
    val discountedTotal: Double,
    val id: Int = 0,
    val products: List<Product> = emptyList(),
    val total: Double = 0.0,
    val totalProducts: Int = 0,
    val totalQuantity: Int = 0,
    val userId: Int,
    val date: LocalDate? = null
)

data class Product(
    val discountPercentage: Double,
    val discountedTotal: Double,
    val id: Int,
    val price: Double,
    val quantity: Int,
    val thumbnail: String,
    val title: String,
    val total: Double
)