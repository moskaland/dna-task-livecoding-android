package com.devmoskal.core.common

/**
 *  Represents the failure side of [Result] of operation , either [Success] or [Failure]
 **/
sealed class Result<out DATA, out ERROR> {
    data class Success<out DATA>(val successValue: DATA) : Result<DATA, Nothing>()

    data class Failure<out ERROR>(val failureValue: ERROR) : Result<Nothing, ERROR>()

    val isSuccess get() = this is Success<DATA>
    val isFailure get() = this is Failure<ERROR>

    suspend fun onFailure(onFailure: suspend (ERROR) -> Unit): Result<DATA, ERROR> {
        if (this is Failure<ERROR>) onFailure(failureValue)
        return this
    }

    suspend fun onSuccess(onSuccess: suspend (DATA) -> Unit): Result<DATA, ERROR> {
        if (this is Success<DATA>) onSuccess(successValue)
        return this
    }
}