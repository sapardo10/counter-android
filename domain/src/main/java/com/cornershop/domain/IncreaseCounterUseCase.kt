package com.cornershop.domain

import com.cornershop.data.models.Counter
import com.cornershop.data.models.Result
import com.cornershop.data.repositories.ICounterRepository

/**
 * Interface that will hold the contract for the use case to increase a counter by any given amount
 */
interface IIncreaseCounterUseCase {

    /**
     * Invocation of the use case
     * @param counterToIncrement [Counter] that the user wants to increment
     * @param increment [Int] that indicates how much does the user want to increment the [Counter]
     * @return [Result] as [Result.Success] with [Boolean] or as [Result.Failure] with the failure
     * reason
     */
    suspend operator fun invoke(counterToIncrement: Counter, increment: Int = 1): Result<Boolean>
}

/**
 * Implementation of [IIncreaseCounterUseCase]
 */
class IncreaseCounterUseCase(
        private val counterRepository: ICounterRepository
): IIncreaseCounterUseCase {
    override suspend operator fun invoke(counterToIncrement: Counter, increment: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }
}