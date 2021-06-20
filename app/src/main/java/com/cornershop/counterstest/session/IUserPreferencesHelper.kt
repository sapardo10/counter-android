package com.cornershop.counterstest.session

/**
 * Interface that holds the contract for the class that will act as a wrapper for the shared
 * preferences API
 */
interface IUserPreferencesHelper {

    /**
     * Method that tells if the user has already seen the welcome screen before.
     * @return true if it has, false otherwise.
     */
    fun hasUserSeenWelcomeScreenBefore(): Boolean

    /**
     * Method that saves the info whether the user has already seen the welcome screen or not
     * @param hasSeenIt [Boolean] true if the user should not be shown the welcome screen, false
     * otherwise.
     */
    fun setHasUserSeenWelcomeScreenBefore(hasSeenIt: Boolean)
}