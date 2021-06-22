package com.cornershop.counterstest.database.daos

import androidx.room.*
import com.cornershop.counterstest.database.entities.CounterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDAO {

    @Delete
    fun delete(counter: CounterEntity)

    @Query("SELECT * FROM counters")
    fun getAll(): Flow<List<CounterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setAll(counters: List<CounterEntity>)
}