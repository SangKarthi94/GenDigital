package com.infiquity.mahindra.ui.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infiquity.mahindra.domain.core.Result
import com.infiquity.mahindra.domain.usecase.GetCartListUseCase
import com.infiquity.mahindra.ui.dashboard.model.Cart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getCartListUseCase: GetCartListUseCase,
) : ViewModel() {

    private val _cartListStateFlow = MutableStateFlow(CartViewState())
    val cartListStateFlow: StateFlow<CartViewState> = _cartListStateFlow

    private val _selectedFilter = MutableStateFlow(CartFilterOption.WEEKLY)
    val selectedFilter: StateFlow<CartFilterOption> = _selectedFilter

    init {
        getCartList()
    }

    private fun getCartList() {
        viewModelScope.launch {
            _cartListStateFlow.update { it.copy(isLoading = true) }

            val result = withTimeoutOrNull(30_000) {
                try {
                    getCartListUseCase(Unit)
                } catch (e: UnknownHostException) {
                    Result.Error(e)
                } catch (e: Exception) {
                    Result.Error(e)
                }
            }

            if (result == null) {
                /*Handle timeout error (If it occurs please Invalidate and restart android studio,
                Its happening time to time due to URL issue)*/
                _cartListStateFlow.update {
                    it.copy(isLoading = false, error = "Request timed out. Please try again.")
                }
            } else {
                when (result) {
                    is Result.Error -> {
                        _cartListStateFlow.update {
                            val errorMessage = if (result.exception is UnknownHostException) {
                                "Network error: Unable to resolve host. Please check your internet connection."
                            } else {
                                result.exception.message ?: "Unknown error"
                            }
                            it.copy(error = errorMessage, isLoading = false)
                        }
                    }

                    is Result.Loading -> {
                        _cartListStateFlow.update { it.copy(isLoading = true) }
                    }

                    is Result.Success -> {
                        // Add random dates to the carts
                        val cartsWithDates = assignRandomDatesToCarts(result.data.carts)
                        allCartListData = cartsWithDates // Store the full list with random dates

                        // Filter the data based on the selected filter
                        val filteredList = when (_selectedFilter.value) {
                            CartFilterOption.WEEKLY -> {
                                allCartListData.filter { isWithinLast7Days(it) }
                            }
                            CartFilterOption.MONTHLY -> {
                                allCartListData.filter { isWithinCurrentMonth(it) }
                            }
                        }

                        // Update the state with the filtered data
                        _cartListStateFlow.update {
                            it.copy(
                                filteredCartListData = filteredList,
                                isLoading = false,
                                totalExpenses = filteredList.sumOf { cart -> cart.total },
                                energyConsumed = filteredList.sumOf { cart -> cart.totalProducts }
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateFilter(filter: CartFilterOption) {
        _selectedFilter.value = filter
        applyFilter(filter)
    }

    private fun applyFilter(filter: CartFilterOption) {
        val filteredList = when (filter) {
            CartFilterOption.WEEKLY -> {
                allCartListData.filter { isWithinLast7Days(it) }
            }
            CartFilterOption.MONTHLY -> {
                allCartListData.filter { isWithinCurrentMonth(it) }
            }
        }

        _cartListStateFlow.update {
            it.copy(
                filteredCartListData = filteredList,
                totalExpenses = filteredList.sumOf { cart -> cart.total },
                energyConsumed = filteredList.sumOf { cart -> cart.totalProducts }
            )
        }
    }

    private fun isWithinLast7Days(cart: Cart): Boolean {
        return cart.date in getLast7Days() // Ensure `cart.date` is in a compatible format (e.g., `LocalDate`)
    }

    private fun isWithinCurrentMonth(cart: Cart): Boolean {
        return cart.date in getLast30Days()
    }

    /*
    // If we want to filter data based on only the current month then use this method
    private fun isWithinCurrentMonth(cart: Cart): Boolean {
        return  cart.date.month == LocalDate.now().month
    }
    */

    private fun getLast7Days(): List<LocalDate> {
        val today = LocalDate.now()
        return (0..6).map { today.minusDays(it.toLong()) }
    }

    private fun getLast30Days(): List<LocalDate> {
        val today = LocalDate.now()
        return (0..29).map { today.minusDays(it.toLong()) }
    }

    fun getLast7DaysDateRange(is7Days: Boolean): String {
        // Create a calendar instance for today's date
        val calendar = Calendar.getInstance()

        // Format for the dates
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        // Get today's date
        val today = calendar.time
        val todayFormatted = dateFormat.format(today)

        if (is7Days)
            calendar.add(Calendar.DAY_OF_YEAR, -6) // Include today, so 7 days total
        else
            calendar.add(Calendar.DAY_OF_YEAR, -29) // Include today, so 30 days total

        val sevenDaysAgo = calendar.time
        val sevenDaysAgoFormatted = dateFormat.format(sevenDaysAgo)

        // Combine the dates into the required range format
        return "$sevenDaysAgoFormatted - $todayFormatted"
    }

    fun getFilter(): CartFilterOption {
        return _selectedFilter.value
    }

    // Add this property to store all data
    private var allCartListData: List<Cart> = emptyList()

    data class CartViewState(
        val filteredCartListData: List<Cart> = emptyList(),
        val isLoading: Boolean = false,
        val error: String = "",
        val totalExpenses: Double = 0.0,
        val energyConsumed: Int = 0
    )

    fun dismissErrorDialog() {
        _cartListStateFlow.update {
            it.copy(error = "")
        }
    }

}

fun assignRandomDatesToCarts(cartList: List<Cart>): List<Cart> {
    return cartList.map { cart ->
        cart.copy(date = getRandomDateWithinLastTwoMonths())
    }
}

fun getRandomDateWithinLastTwoMonths(): LocalDate {
    val today = LocalDate.now()
    val twoMonthsAgo = today.minusMonths(2)

    // Generate a random number of days between twoMonthsAgo and today
    val randomDays = Random.nextLong(ChronoUnit.DAYS.between(twoMonthsAgo, today) + 1)
    return twoMonthsAgo.plusDays(randomDays)
}

enum class CartFilterOption {
    MONTHLY,
    WEEKLY
}