package com.cornershop.counterstest.datasources

import com.cornershop.counterstest.database.daos.CounterDAO
import com.cornershop.counterstest.database.entities.CounterEntity
import com.cornershop.data.datasources.ICounterLocalDataSource
import com.cornershop.data.models.Counter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CounterLocalDataSource(
    private val counterDAO: CounterDAO
): ICounterLocalDataSource {

    override suspend fun getAll(): Flow<List<Counter>> {
        return withContext(Dispatchers.IO) {
            counterDAO.getAll().map { entityList ->
                entityList.map { entity -> Counter(
                    count = entity.count,
                    id = entity.id,
                    name = entity.title
                ) }
            }
        }
    }

    override suspend fun setAll(counters: List<Counter>) {
        withContext(Dispatchers.IO) {
            counterDAO.setAll(
                counters = counters.map { model -> CounterEntity(
                    id = model.id,
                    count = model.count,
                    title = model.name
                ) }
            )
        }
    }


}