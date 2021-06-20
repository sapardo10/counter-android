package com.cornershop.data.datasources

import com.cornershop.data.models.Counter

/**
 * Interface that will hold the contract for the data source in charge of all transactions related
 * to [Counter] that are performed locally on the device.
 */
interface ICounterLocalDataSource {

    suspend fun getAll()

}