package com.cornershop.counterstest.features.welcome

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.cornershop.counterstest.R
import com.cornershop.counterstest.core.BaseActivity
import com.cornershop.counterstest.databinding.ActivityWelcomeBinding
import com.cornershop.counterstest.features.main.MainActivity
import com.cornershop.counterstest.utils.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WelcomeActivity : BaseActivity() {

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

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
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
                    WelcomeViewModelActions.SHOW_VIEWS -> binding.welcomeContent.root.visibility =
                        View.VISIBLE
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
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
        finish()
    }

}
