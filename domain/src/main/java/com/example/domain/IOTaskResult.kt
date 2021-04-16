package com.example.domain

/**
 * Sealed class type-restricts the result of IO calls to success and failure. The type
 * <T> represents the model class expected from the API call in case of a success
 * In case of success, the result will be wrapped around the OnSuccessResponse class
 * In case of error, the throwable causing the error will be wrapped around OnErrorResponse class
 * @author Cato
 * @since 1.0
 */

sealed class IOTaskResult<out DTO : Any> {
    data class OnSuccess<out DTO : Any>(val data: DTO) : IOTaskResult<DTO>()
    data class OnFailed(val throwable: Throwable) : IOTaskResult<Nothing>()
}