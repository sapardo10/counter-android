package com.cornershop.data.datasources

import com.cornershop.data.models.Counter
import com.cornershop.data.models.Result

/**
 * Interface that will hold the contract for the data source in charge of all transactions related
 * to [Counter] that are performed against a server (remotely).
 */
interface ICounterRemoteDataSource {

    suspend fun getAll(): Result<List<Counter>>

}