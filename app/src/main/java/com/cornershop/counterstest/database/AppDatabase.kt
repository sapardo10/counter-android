package com.cornershop.counterstest.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cornershop.counterstest.database.daos.CounterDAO
import com.cornershop.counterstest.database.entities.CounterEntity

@Database(entities = [CounterEntity::class], exportSchema = false, version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun counterDao(): CounterDAO
}