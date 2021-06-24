package com.cornershop.data.datasources

import com.cornershop.data.models.Counter
import com.cornershop.data.models.SuggestionsCategory
import kotlinx.coroutines.flow.Flow

/**
 * Interface that will hold the contract for the data source in charge of all transactions related
 * to [Counter] that are performed locally on the device.
 */
interface ICounterLocalDataSource {

    /**
     * Method that deletes the [Counter] passed as parameter on the local database
     * @param counter [Counter] to be deleted
     */
    suspend fun deleteCounter(counter: Counter)

    /**
     * Method that returns a flow to watch all the changes related to counters on the local
     * database.
     * @return [Flow] with [List] of [Counter] inside the local database
     */
    suspend fun getAllCounters(): Flow<List<Counter>>

    /**
     * Method that returns all the [SuggestionsCategory] from the local application resources
     * @return [List] of [SuggestionsCategory] saved locally on the app
     */
    fun getAllCounterCategorySuggestions(): List<SuggestionsCategory>

    /**
     * Method that updated the local database with the [List] of [Counter] passed as parameter
     * @param counters [List] of [Counter] to be updated
     */
    suspend fun setAllCounters(counters: List<Counter>)

}