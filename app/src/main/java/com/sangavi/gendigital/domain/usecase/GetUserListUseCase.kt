package com.sangavi.gendigital.domain.usecase


import com.sangavi.gendigital.di.qualifier.IoDispatcher
import com.sangavi.gendigital.data.repository.DataRepository
import com.sangavi.gendigital.domain.core.UseCase
import com.sangavi.gendigital.model.UserListResponseModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetUserListUseCase @Inject constructor(
    private val userRepository: DataRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Unit, List<UserListResponseModel>>(dispatcher) {

    override suspend fun execute(parameters: Unit): List<UserListResponseModel> {
        return userRepository.getUserList()
    }
}
