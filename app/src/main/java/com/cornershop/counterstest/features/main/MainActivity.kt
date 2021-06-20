package com.cornershop.counterstest.features.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.cornershop.counterstest.R
import com.cornershop.counterstest.core.BaseActivity
import com.cornershop.counterstest.databinding.ActivityMainBinding
import com.cornershop.counterstest.utils.setOnSingleClickListener
import com.cornershop.counterstest.features.create.CreateActivity
import com.cornershop.counterstest.features.main.loading.MainLoadingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: BaseActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    /**
     * ------------------------------------ LIFECYCLE METHODS --------------------------------------
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeObservers()
        initializeInteractionsListener()
        viewModel.initializeView()
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<MainLoadingFragment>(R.id.fragment_container_view)
        }
    }

    /**
     * -------------------------------------- PRIVATE METHODS --------------------------------------
     */

    /**
     * Method that initializes the interactions the view can have
     */
    private fun initializeInteractionsListener() {
        binding.addNewCounterButton.setOnSingleClickListener {
            viewModel.onCreateCounterButtonTapped()
        }
    }

    /**
     * Method that initializes the actions observer
     */
    private fun initializeActionsObserver() {
        viewModel.actions.observe(this, {
            it?.let { action ->
                when (action) {
                    is MainViewModelActions.GoToCreateScreen -> navigateToCreateScreen()
                    is MainViewModelActions.ShowListCounterNoInternetConnectionDialog -> println("show list counter no internet connection dialog")
                    is MainViewModelActions.ShowUpdateCounterNoInternetConnectionDialog -> println("show update counter no internet connection dialog")
                }
            }
        })
    }

    /**
     * Method that initializes the counters observer
     */
    private fun initializeCountersObserver() {
        viewModel.counters.observe(this, {
            //TODO(Update recycler view)
        })
    }

    /**
     * Method that initializes the observers over the live data on the view model
     */
    private fun initializeObservers() {
        initializeActionsObserver()
        initializeCountersObserver()
    }

    /**
     * Method that directs the user to the main screen of hte application
     */
    private fun navigateToCreateScreen() {
        val intent = Intent(this, CreateActivity::class.java)
        startActivity(intent)
    }

}