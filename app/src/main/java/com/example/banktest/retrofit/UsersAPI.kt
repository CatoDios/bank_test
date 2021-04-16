package com.example.banktest.retrofit

import com.example.domain.User

import retrofit2.Response
import retrofit2.http.*
import javax.inject.Singleton

/**
 * Retrofit API class
 * @author Cato
 * @since 1.0
 */
@Singleton
interface UsersAPI {

    /**
     * realiza un  PUT call para mandar al servidor nuestro token,
     * para que el server pueda enviar mensajes (Firebase Message)
     * @param token el token de nuestro celular
     * @param auth token del usuario
     * @return [MessageEntity] solo nos da un mensaje de confirmacion que solo se usa en el Logcat
     */

    @GET("users")
    suspend fun getUsers(
    ): Response<List<User>>



}