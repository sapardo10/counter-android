package com.cornershop.counterstest.network.dtos

import com.google.gson.annotations.SerializedName

data class CounterDTO (
    @SerializedName("count")
    val count: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String
)