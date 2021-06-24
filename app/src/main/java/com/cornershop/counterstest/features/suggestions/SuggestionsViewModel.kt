package com.cornershop.counterstest.features.suggestions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cornershop.data.models.Suggestion
import com.cornershop.domain.IGetAllSuggestionsCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SuggestionsViewModel @Inject constructor(
    private val getAllSuggestionsCategoriesUseCase: IGetAllSuggestionsCategoriesUseCase
) : ViewModel() {

    val actions = MutableLiveData<SuggestionsViewModelActions>()

    fun getSuggestionsCategoriesViewModels(): List<CategorySuggestionViewModel> {
        return getAllSuggestionsCategoriesUseCase()
            .map { category ->
                CategorySuggestionViewModel(
                    title = category.title,
                    suggestions = category.suggestions.map {
                        SuggestionItemViewModel(name = it.name, onTap = {
                            onItemTapped(it)
                        })
                    }
                )
            }
    }

    private fun onItemTapped(suggestion: Suggestion) {
        actions.postValue(SuggestionsViewModelActions.NavigateBack(suggestion = suggestion))
    }
}

sealed class SuggestionsViewModelActions {
    class NavigateBack(val suggestion: Suggestion) : SuggestionsViewModelActions()
}