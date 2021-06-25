package com.cornershop.counterstest.features.suggestions

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cornershop.counterstest.R
import com.cornershop.counterstest.core.BaseActivity
import com.cornershop.counterstest.databinding.ActivitySuggestionsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SuggestionsActivity : BaseActivity() {

    private val viewModel: SuggestionsViewModel by viewModels()

    private lateinit var binding: ActivitySuggestionsBinding

    /**
     * ------------------------------------ LIFECYCLE METHODS --------------------------------------
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuggestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureToolbar()
        configureList()
        initializeObservers()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * ------------------------------------- PRIVATE METHODS ---------------------------------------
     */

    private fun configureList() {
        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            adapter =
                SuggestionsCategoryAdapter(
                    viewModel.getSuggestionsCategoriesViewModels()
                )
        }
    }

    /**
     * Method that configures all the icons, texts and interactions of the toolbar
     */
    private fun configureToolbar() {
        with(binding.toolbar) {
            setSupportActionBar(this)
            supportActionBar?.title = getString(R.string.examples)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    /**
     * Method that initializes the observers over the view model attributes
     */
    private fun initializeObservers() {
        viewModel.actions.observe(this, {
            it?.let { action ->
                when (action) {
                    is SuggestionsViewModelActions.NavigateBack -> {
                        val returnIntent = Intent()
                        returnIntent.putExtra(SUGGESTION_NAME_KEY, action.suggestion.name)
                        setResult(RESULT_OK, returnIntent)
                        finish()
                    }
                }
            }
        })
    }

    companion object {
        const val SUGGESTION_NAME_KEY = "suggestion"
    }
}