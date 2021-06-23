package com.cornershop.data.repositories

import com.cornershop.data.models.Counter
import com.cornershop.data.models.CounterError
import com.cornershop.data.models.Result
import kotlinx.coroutines.flow.Flow

/**
 * Interface holding the contract for the repository that will handle all data transactions
 * related to [Counter]
 */
interface ICounterRepository {

    /**
     * Method that creates a new counter
     * @param title [String] name of the counter to be created
     * @return If success, a [Boolean]. If failure, a [CounterError]
     */
    suspend fun createCounter(title: String): Result<Boolean>

    /**
     * Method that decreases by one the count of a counter
     * @param counter [Counter] to be decreased
     * @return If success, a [Boolean]. If failure, a [CounterError]
     */
    suspend fun decreaseCounter(counter: Counter): Result<Boolean>

    /**
     * Method that deletes a counter
     * @param counter [Counter] representing the counter that should be deleted
     * @return If success, a [Boolean]. If failure, a [CounterError]
     */
    suspend fun deleteCounter(counter: Counter): Result<Boolean>

    /**
     * Method that returns all the counters available for the user
     * @return If success, [List] of [Counter]. If failure, a [CounterError]
     */
    suspend fun getAll(): Flow<Result<List<Counter>>>

    /**
     * Method that increases by one the count of a counter
     * @param counter [Counter] to be increased
     * @return If success, a [Boolean]. If failure, a [CounterError]
     */
    suspend fun increaseCounter(counter: Counter): Result<Boolean>
}