package com.infiquity.mahindra.domain.usecase


import com.infiquity.mahindra.data.repository.DataRepository
import com.infiquity.mahindra.di.qualifier.IoDispatcher
import com.infiquity.mahindra.domain.core.UseCase
import com.infiquity.mahindra.ui.dashboard.model.CartListData
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetCartListUseCase @Inject constructor(
    private val userRepository: DataRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Unit, CartListData>(dispatcher) {

    override suspend fun execute(parameters: Unit): CartListData {
        return userRepository.getCartListData()
    }
}
