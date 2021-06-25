package com.cornershop.counterstest.datasources

import android.content.Context
import com.cornershop.counterstest.R
import com.cornershop.counterstest.database.daos.CounterDAO
import com.cornershop.data.datasources.ICounterLocalDataSource
import com.cornershop.data.models.Counter
import com.cornershop.data.models.Suggestion
import com.cornershop.data.models.SuggestionsCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Implementation of [ICounterLocalDataSource]
 */
class CounterLocalDataSource(
    private val context: Context,
    private val counterDAO: CounterDAO
) : ICounterLocalDataSource {

    /**
     * -------------------------------------- PUBLIC METHODS ---------------------------------------
     */

    override suspend fun decreaseCounter(counter: Counter) {
        withContext(Dispatchers.IO) {
            counter.count = counter.count - 1
            counterDAO.updateCounter(counter.toCounterEntity())
        }
    }

    override suspend fun deleteCounter(counter: Counter) {
        withContext(Dispatchers.IO) {
            counterDAO.delete(counter.toCounterEntity())
        }
    }

    override suspend fun getAllCounters(): Flow<List<Counter>> {
        return withContext(Dispatchers.IO) {
            counterDAO.getAll().map { entityList ->
                entityList.map {
                    it.toCounter()
                }
            }
        }
    }

    override fun getAllCounterCategorySuggestions(): List<SuggestionsCategory> {
        return listOf(
            SuggestionsCategory(
                title = context.getString(R.string.drinks),
                suggestions = context.resources.getStringArray(R.array.drinks_array)
                    .map { Suggestion(it) }
            ),
            SuggestionsCategory(
                title = context.getString(R.string.food),
                suggestions = context.resources.getStringArray(R.array.food_array)
                    .map { Suggestion(it) }
            ),
            SuggestionsCategory(
                title = context.getString(R.string.misc),
                suggestions = context.resources.getStringArray(R.array.misc_array)
                    .map { Suggestion(it) }
            )
        )
    }

    override suspend fun increaseCounter(counter: Counter) {
        withContext(Dispatchers.IO) {
            counter.count = counter.count + 1
            counterDAO.updateCounter(counter.toCounterEntity())
        }
    }

    override suspend fun setAllCounters(counters: List<Counter>) {
        withContext(Dispatchers.IO) {
            counterDAO.setAll(
                counters = counters.map {
                    it.toCounterEntity()
                }
            )
        }
    }

}