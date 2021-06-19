package com.cornershop.domain

import com.cornershop.data.models.Counter
import com.cornershop.data.models.Result
import com.cornershop.data.repositories.ICounterRepository

/**
 * Interface that will hold the contract for the use case to decrease a counter by any given amount
 */
interface IDecreaseCounterUseCase {

    /**
     * Invocation of the use case
     * @param counterToDecrement [Counter] that the user wants to decrement
     * @param decrement [Int] that indicates how much does the user want to decrement the [Counter]
     * @return [Result] as [Result.Success] with [Boolean] or as [Result.Failure] with the failure
     * reason
     */
    suspend operator fun invoke(counterToDecrement: Counter, decrement: Int = 1): Result<Boolean>
}

/**
 * Implementation of [IDecreaseCounterUseCase]
 */
class DecreaseCounterUseCase(
        private val counterRepository: ICounterRepository
): IDecreaseCounterUseCase {
    override suspend operator fun invoke(counterToDecrement: Counter, decrement: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }
}