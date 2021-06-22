package com.cornershop.data.datasources

import com.cornershop.data.models.Counter
import com.cornershop.data.models.Result

/**
 * Interface that will hold the contract for the data source in charge of all transactions related
 * to [Counter] that are performed against a server (remotely).
 */
interface ICounterRemoteDataSource {

    /**
     * Method that calls the remote service to decrease the count of a counter by one
     * @param counter [Counter] to be updated
     * @return [Result.Success] if the operation was completed successfully, [Result.Failure] with
     * the failure reason otherwise
     */
    suspend fun decreaseCounter(counter: Counter): Result<List<Counter>>

    /**
     * Method that calls the remote service to delete a counter
     * @param counter [Counter] to be deleted
     * @return [Result.Success] if the operation was completed successfully, [Result.Failure] with
     * the failure reason otherwise
     */
    suspend fun deleteCounter(counter: Counter): Result<List<Counter>>

    /**
     * Method that calls the remote service to get all the counters
     * @return [Result.Success] if the operation was completed successfully with the list inside,
     * [Result.Failure] with the failure reason otherwise
     */
    suspend fun getAll(): Result<List<Counter>>

    /**
     * Method that calls the remote service to increase the count of a counter by one
     * @param counter [Counter] to be updated
     * @return [Result.Success] if the operation was completed successfully, [Result.Failure] with
     * the failure reason otherwise
     */
    suspend fun increaseCounter(counter: Counter): Result<List<Counter>>

}