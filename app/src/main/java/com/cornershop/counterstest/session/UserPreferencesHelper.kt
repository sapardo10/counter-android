package com.cornershop.counterstest.session

import android.content.Context
import android.content.SharedPreferences
import com.cornershop.data.constants.ApplicationConstants.SHARED_PREFERENCES_KEY_USER_HAS_SEEN_WELCOME_SCREEN
import com.cornershop.data.constants.ApplicationConstants.SHARED_PREFERENCES_USER_PREFERENCES_PATH

/**
 * Implementation of [IUserPreferencesHelper]
 */
class UserPreferencesHelper(
    context: Context
) : IUserPreferencesHelper {

    private var sharedPreferences: SharedPreferences? =
        context.getSharedPreferences(SHARED_PREFERENCES_USER_PREFERENCES_PATH, Context.MODE_PRIVATE)

    /**
     * -------------------------------------- PUBLIC METHODS ---------------------------------------
     */

    override fun hasUserSeenWelcomeScreenBefore(): Boolean {
        return sharedPreferences?.getBoolean(
            SHARED_PREFERENCES_KEY_USER_HAS_SEEN_WELCOME_SCREEN,
            false
        ) ?: false
    }

    override fun setHasUserSeenWelcomeScreenBefore(hasSeenIt: Boolean) {
        sharedPreferences?.edit()
            ?.putBoolean(SHARED_PREFERENCES_KEY_USER_HAS_SEEN_WELCOME_SCREEN, hasSeenIt)?.apply()
    }

}