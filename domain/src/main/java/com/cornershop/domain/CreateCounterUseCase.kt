package com.cornershop.domain

import com.cornershop.data.models.Counter
import com.cornershop.data.models.Result
import com.cornershop.data.repositories.CounterRepository
import javax.inject.Inject

/**
 * Interface that will hold the contract for the use case to create a new counter
 */
interface ICreateCounterUseCase {

    /**
     * Invocation of the use case
     * @return [Result] if success returns a [Counter] within. If failed it returns the failure
     * reason
     */
    suspend operator fun invoke(): Result<Counter>
}

/**
 * Implementation of [ICreateCounterUseCase]
 */
class CreateCounterUseCase @Inject constructor(
        private val counterRepository: CounterRepository
): ICreateCounterUseCase {
    override suspend operator fun invoke(): Result<Counter> {
        val result = counterRepository.createCounter()
        return Result.Success(data = Counter(2,1,"sadsad"))
    }
}