package com.example.banktest.datasources

import com.example.banktest.retrofit.UsersAPI
import com.example.banktest.utils.performSafeNetworkApiCall
import com.example.data.RemoteDataSource
import com.example.domain.IOTaskResult
import com.example.domain.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserNetworkDataSource @Inject constructor(private val retrofitClient: UsersAPI) :
    RemoteDataSource {

    override suspend fun getUsers(quantity: Int): Flow<IOTaskResult<List<User>>> =
        performSafeNetworkApiCall("Error getting users") {
            retrofitClient.getUsers()
        }



}