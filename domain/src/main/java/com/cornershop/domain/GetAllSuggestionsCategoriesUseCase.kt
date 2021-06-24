package com.cornershop.domain

import com.cornershop.data.models.SuggestionsCategory
import com.cornershop.data.repositories.ICounterRepository
import javax.inject.Inject

/**
 * Interface that holds the contract for the use case that will return all the suggestions
 * categories available for the application
 */
interface IGetAllSuggestionsCategoriesUseCase {

    /**
     * Method that returns a [List] of [SuggestionsCategory]
     * @return [List] of [SuggestionsCategory]
     */
    operator fun invoke(): List<SuggestionsCategory>
}

/**
 * Implementation of [IGetAllSuggestionsCategoriesUseCase]
 */
class GetAllSuggestionsCategoriesUseCase @Inject constructor(
    private val counterRepository: ICounterRepository
) : IGetAllSuggestionsCategoriesUseCase {

    override operator fun invoke(): List<SuggestionsCategory> {
        return counterRepository.getAllCategorySuggestions()
    }
}