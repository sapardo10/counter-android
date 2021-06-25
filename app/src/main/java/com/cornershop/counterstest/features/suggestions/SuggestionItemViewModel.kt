package com.cornershop.counterstest.features.suggestions

/**
 * View Model for a suggestion item
 */
data class SuggestionItemViewModel(
    val name: String,
    val onTap: () -> Unit
)
