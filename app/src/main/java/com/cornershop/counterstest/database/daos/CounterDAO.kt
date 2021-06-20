package com.cornershop.counterstest.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cornershop.counterstest.database.entities.CounterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDAO {
    @Query("SELECT * FROM counters")
    fun getAll(): Flow<List<CounterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setAll(counters : List<CounterEntity>)
}