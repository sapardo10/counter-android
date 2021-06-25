package com.cornershop.counterstest.features.suggestions

/**
 * View model for a category of suggestions
 */
data class CategorySuggestionViewModel(
    val title: String,
    val suggestions: List<SuggestionItemViewModel>
)
