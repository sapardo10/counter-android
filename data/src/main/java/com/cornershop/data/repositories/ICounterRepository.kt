package com.cornershop.data.repositories

import com.cornershop.data.models.Counter
import com.cornershop.data.models.CounterError
import com.cornershop.data.models.Result

/**
 * Interface holding the contract for the repository that will handle all data transactions
 * related to [Counter]
 */
interface ICounterRepository {

    /**
     * Method that creates a new counter
     * @return If success, a [Boolean]. If failure, a [CounterError]
     */
    suspend fun createCounter(): Result<Boolean>

    /**
     * Method that decreases by one the count of a counter
     * @param counterId [Int] representing the id of the counter that should be decreased
     * @return If success, a [Boolean]. If failure, a [CounterError]
     */
    suspend fun decreaseCounter(counterId: Int): Result<Boolean>

    /**
     * Method that deletes a counter
     * @param counterId [Int] representing the id of the counter that should be deleted
     * @return If success, a [Boolean]. If failure, a [CounterError]
     */
    suspend fun deleteCounter(counterId: Int): Result<Boolean>

    /**
     * Method that returns all the counters available for the user
     * @return If success, [List] of [Counter]. If failure, a [CounterError]
     */
    suspend fun getAll(): Result<List<Counter>>

    /**
     * Method that increases by one the count of a counter
     * @param counterId [Int] representing the id of the counter that should be increased
     * @return If success, a [Boolean]. If failure, a [CounterError]
     */
    suspend fun increaseCounter(counterId: Int): Result<Boolean>
}