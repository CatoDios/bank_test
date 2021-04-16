package com.example.data

import com.example.domain.IOTaskResult
import com.example.domain.User
import kotlinx.coroutines.flow.Flow

class UserRepository constructor(private val remoteDataSource: RemoteDataSource) : Repository {

    override suspend fun getUsers(quantity: Int) : Flow<IOTaskResult<List<User>>> =
        remoteDataSource.getUsers(quantity = quantity)


}