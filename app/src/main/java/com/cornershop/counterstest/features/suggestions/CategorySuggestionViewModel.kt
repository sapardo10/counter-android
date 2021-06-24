package com.cornershop.counterstest.features.suggestions

data class CategorySuggestionViewModel(
    val title: String,
    val suggestions: List<SuggestionItemViewModel>
)
