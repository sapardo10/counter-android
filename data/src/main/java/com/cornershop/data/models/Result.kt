package com.cornershop.data.models

/**
 * Class that will be used by different architecture layers to respond a transaction and give the
 * data if success or give an inside peek if it was a failure.
 */
sealed class Result<T> {

    /**
     * Class that will mean a failure in a transaction and that will have an error inside to give
     * more info about what happened.
     */
    data class Failure<T>(val error: CounterError) : Result<T>()

    /**
     * Class that will mean a success in a transaction and that it will hold the data, if any, from
     * it
     */
    data class Success<T>(val data: T) : Result<T>()


}