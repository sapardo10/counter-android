package com.cornershop.counterstest.features.welcome

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.ActivityWelcomeBinding
import com.cornershop.counterstest.extensions.setOnSingleClickListener
import com.cornershop.counterstest.features.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {

    private val viewModel: WelcomeViewModel by viewModels()

    private lateinit var binding: ActivityWelcomeBinding

    /**
     * ------------------------------------ LIFECYCLE METHODS --------------------------------------
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeObservers()
        initializeInteractionsListener()
        viewModel.initializeView()
    }

    /**
     * -------------------------------------- PRIVATE METHODS ---------------------------------------
     */

    /**
     * Method that initializes the interactions the view can have
     */
    private fun initializeInteractionsListener() {
        binding.welcomeContent?.buttonStart?.setOnSingleClickListener {
            viewModel.onStartButtonTapped()
        }
    }

    /**
     * Method that initializes the observers over the live data on the view model
     */
    private fun initializeObservers() {
        viewModel.actions.observe(this, {
            it?.let { action ->
                when (action) {
                    WelcomeViewModelActions.GO_TO_MAIN_SCREEN -> navigateToMainScreen()
                }
            }
        })
    }

    /**
     * Method that directs the user to the main screen of hte application
     */
    private fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
