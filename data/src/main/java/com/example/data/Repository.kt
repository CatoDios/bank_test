package com.example.data

import com.example.domain.IOTaskResult
import com.example.domain.User
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getUsers(quantity: Int): Flow<IOTaskResult<List<User>>>

}