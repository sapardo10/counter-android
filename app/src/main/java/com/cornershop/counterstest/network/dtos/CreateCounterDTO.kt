package com.cornershop.counterstest.network.dtos

import com.google.gson.annotations.SerializedName

data class CreateCounterDTO(
    @SerializedName("title")
    private val title: String
)