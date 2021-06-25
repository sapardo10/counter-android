package com.cornershop.counterstest.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "counters")
data class CounterEntity(
    @PrimaryKey
    val id: String,
    val count: Int,
    val title: String
)