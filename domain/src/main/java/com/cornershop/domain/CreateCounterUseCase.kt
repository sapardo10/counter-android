package com.cornershop.domain

import com.cornershop.data.models.Result
import com.cornershop.data.repositories.ICounterRepository

/**
 * Interface that will hold the contract for the use case to create a new counter
 */
interface ICreateCounterUseCase {

    /**
     * Invocation of the use case
     * @param title [String] name of the counter to be created
     * @return [Result] if success returns a [Boolean] within. If failed it returns the failure
     * reason
     */
    suspend operator fun invoke(title: String): Result<Boolean>
}

/**
 * Implementation of [ICreateCounterUseCase]
 */
class CreateCounterUseCase(
    private val counterRepository: ICounterRepository
) : ICreateCounterUseCase {

    /**
     * -------------------------------------- PUBLIC METHODS ---------------------------------------
     */

    override suspend operator fun invoke(title: String): Result<Boolean> {
        return counterRepository.createCounter(title = title)
    }
}