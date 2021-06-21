package com.cornershop.counterstest.datasources

import com.cornershop.counterstest.database.entities.CounterEntity
import com.cornershop.counterstest.network.dtos.CounterDTO
import com.cornershop.data.models.Counter

fun Counter.toCounterDTO() =
    CounterDTO(
        count = count,
        id = id,
        title = name
    )

fun Counter.toCounterEntity() =
    CounterEntity(
        count = count,
        id = id,
        title = name
    )

fun CounterDTO.toCounter() =
    Counter(
        count = count,
        id = id,
        name = title
    )

fun CounterEntity.toCounter() =
    Counter(
        count = count,
        id = id,
        name = title
    )