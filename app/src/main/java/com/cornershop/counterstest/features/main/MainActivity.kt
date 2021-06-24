package com.cornershop.counterstest.features.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.cornershop.counterstest.R
import com.cornershop.counterstest.core.BaseActivity
import com.cornershop.counterstest.databinding.ActivityMainBinding
import com.cornershop.counterstest.features.create.CreateActivity
import com.cornershop.counterstest.features.main.emptystate.MainEmptyStateFragment
import com.cornershop.counterstest.features.main.list.MainListFragment
import com.cornershop.counterstest.features.main.loading.MainLoadingFragment
import com.cornershop.counterstest.utils.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

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
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<MainLoadingFragment>(R.id.fragment_container_view)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.initializeView()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
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
                    is MainViewModelActions.ShowEmptyState -> showEmptyState()
                    is MainViewModelActions.ShowListCounter -> showCounterList()
                }
            }
        })
    }

    /**
     * Method that initializes the observers over the live data on the view model
     */
    private fun initializeObservers() {
        initializeActionsObserver()
    }

    /**
     * Method that directs the user to the main screen of hte application
     */
    private fun navigateToCreateScreen() {
        val intent = Intent(this, CreateActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }

    /**
     * Method that displays the list of counters
     */
    private fun showCounterList() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container_view, MainListFragment.newInstance())
        }
    }

    /**
     * Method that displays an empty state on the screen
     */
    private fun showEmptyState() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container_view, MainEmptyStateFragment.newInstance())
        }
    }

}