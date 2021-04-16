package com.example.usecase

import com.example.data.Repository

class GetUsersUseCase  constructor(private val repository : Repository){

    suspend fun getUsers(quantity: Int) =
        repository.getUsers(quantity)
}