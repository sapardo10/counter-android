package com.cornershop.counterstest.features.create


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import com.cornershop.counterstest.R
import com.cornershop.counterstest.core.BaseActivity
import com.cornershop.counterstest.databinding.ActivityCreateBinding
import com.cornershop.counterstest.utils.insertLinks
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CreateActivity : BaseActivity() {

    private val viewModel: CreateViewModel by viewModels()

    private lateinit var binding: ActivityCreateBinding

    /**
     * ------------------------------------ LIFECYCLE METHODS --------------------------------------
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureToolbar()
        configureTextField()
        configureHelpText()
        initializeObservers()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down)
    }

    /**
     * ------------------------------------- PRIVATE METHODS ---------------------------------------
     */

    /**
     * Method that initializes the help text on the screen to be clickable
     */
    private fun configureHelpText() {
        with(binding.helpText) {
            insertLinks(
                Pair(
                    getString(R.string.create_counter_disclaimer_text_link),
                    object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            viewModel.onSeeExamplesClicked()
                        }
                    }
                )
            )
        }
    }

    /**
     * Method that configures the focus for the text field on the screen to be reactive to the
     * user interaction
     */
    private fun configureTextField() {
        with(binding.textField) {
            addTextChangedListener(getTextWatcher())
            setOnFocusChangeListener { _, hasFocus ->
                hint = if (hasFocus) {
                    getString(R.string.counter_name_hint)
                } else {
                    ""
                }
            }
            requestFocus()
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    /**
     * Method that configures all the icons, texts and interactions of the toolbar
     */
    private fun configureToolbar() {
        with(binding.toolbar) {
            title = getString(R.string.create_counter)
            inflateMenu(R.menu.toolbar_create)
            setNavigationIcon(R.drawable.ic_close)
            setNavigationOnClickListener { finish() }
            setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == R.id.save) {
                    viewModel.createCounter()
                }
                return@setOnMenuItemClickListener true
            }
        }
    }

    private fun getTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                ///Do nothing, it does not interest the application so far
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onTextChanged(newText = s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                ///Do nothing, it does not interest the application so far
            }

        }
    }

    /**
     * Method that initializes the observers over the live data on the view model
     */
    private fun initializeObservers() {
        viewModel.actions.observe(this, {
            it?.let { action ->
                when (action) {
                    CreateViewModelActions.GO_TO_EXAMPLES_SCREEN -> print("Examples screen")
                    CreateViewModelActions.HIDE_COUNTER_EMPTY_ERROR -> {
                        with(binding.textField) {
                            error = null
                        }
                    }
                    CreateViewModelActions.HIDE_CREATING_LOADING -> {
                        with(binding.toolbar) {
                            menu.findItem(R.id.save).setActionView(null)
                        }
                    }
                    CreateViewModelActions.NAVIGATE_BACK -> finish()
                    CreateViewModelActions.SHOW_COUNTER_EMPTY_ERROR -> {
                        with(binding.textField) {
                            error = getString(R.string.create_counter_error_blank)
                        }
                    }
                    CreateViewModelActions.SHOW_CREATING_LOADING -> {
                        with(binding.toolbar) {
                            menu.findItem(R.id.save).setActionView(R.layout.toolbar_loader)
                        }
                    }
                    CreateViewModelActions.SHOW_NETWORK_ERROR -> print("network error")
                }
            }
        })
    }
}