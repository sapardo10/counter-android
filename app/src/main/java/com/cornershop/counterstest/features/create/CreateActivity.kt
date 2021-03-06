package com.cornershop.counterstest.features.create

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.cornershop.counterstest.R
import com.cornershop.counterstest.core.BaseActivity
import com.cornershop.counterstest.databinding.ActivityCreateBinding
import com.cornershop.counterstest.features.suggestions.SuggestionsActivity
import com.cornershop.counterstest.utils.buildDialog
import com.cornershop.counterstest.utils.insertLinks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateActivity : BaseActivity() {

    private val viewModel: CreateViewModel by viewModels()

    private lateinit var binding: ActivityCreateBinding

    /**
     * Activity result launcher that will launch the [SuggestionsActivity] and wait for a response
     * in which the user picked a suggestion
     */
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val name = intent?.getStringExtra(SuggestionsActivity.SUGGESTION_NAME_KEY) ?: ""
                with(binding.textField) {
                    setText(name, TextView.BufferType.EDITABLE)
                    setSelection(name.length)
                }
                requestFocusTextField()
            }
        }

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
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.createCounter()
                    return@OnEditorActionListener true
                }
                false
            })
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

    /**
     * Method that returns a [TextWatcher] to implement on the texfield to listen to the changes
     * of it to create a new counter
     */
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
     * Method that hides the error shown on the text field
     */
    private fun hideTextFieldError() {
        with(binding.textField) {
            error = null
        }
    }

    /**
     * Method that hides the loader shown on the toolbar
     */
    private fun hideToolbarLoader() {
        with(binding.toolbar) {
            menu.findItem(R.id.save).setActionView(null)
        }
    }

    /**
     * Method that initializes the observer over the actions on the view model
     */
    private fun initializeObserverOverActions() {
        viewModel.actions.observe(this, { nullableAction ->
            nullableAction?.let { action ->
                when (action) {
                    CreateViewModelActions.GO_TO_EXAMPLES_SCREEN -> navigateToSuggestionsScreen()
                    CreateViewModelActions.HIDE_COUNTER_EMPTY_ERROR -> hideTextFieldError()
                    CreateViewModelActions.HIDE_CREATING_LOADING -> hideToolbarLoader()
                    CreateViewModelActions.NAVIGATE_BACK -> finish()
                    CreateViewModelActions.SHOW_COUNTER_EMPTY_ERROR -> showTextFieldError()
                    CreateViewModelActions.SHOW_CREATING_LOADING -> showToolbarLoader()
                    CreateViewModelActions.SHOW_NETWORK_ERROR -> showNetworkErrorDialog()
                    CreateViewModelActions.SHOW_SOFT_KEYBOARD -> showSoftKeyboard()
                }
            }
        })
    }

    /**
     * Method that initializes the observers over the live data on the view model
     */
    private fun initializeObservers() {
        initializeObserverOverActions()
    }

    /**
     * Method that directs the user to the suggestions screen
     */
    private fun navigateToSuggestionsScreen() {
        startForResult.launch(Intent(this, SuggestionsActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }

    /**
     * Method that request the focus for the text field on the screen
     */
    private fun requestFocusTextField() {
        viewModel.showSoftKeyboard()
    }

    /**
     * Method that builds and displays a dialog stating that the create action could not be complete
     * due to network connection or errors.
     */
    private fun showNetworkErrorDialog() {
        val dialog = buildDialog(
            message = getString(R.string.connection_error_description),
            negativeButtonText = getString(R.string.ok),
            title = getString(
                R.string.error_creating_counter_title
            )
        )
        dialog.show()
    }

    /**
     * Method that shows the soft keyboard to the user so it can input text on the text field
     */
    private fun showSoftKeyboard() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val inputMethodManager: InputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

            inputMethodManager.showSoftInput(
                binding.textField,
                InputMethodManager.SHOW_IMPLICIT
            )
        }
    }

    /**
     * Method that displays the error on the text field
     */
    private fun showTextFieldError() {
        with(binding.textField) {
            error = getString(R.string.create_counter_error_blank)
        }
    }

    /**
     * Method that shows the loader placed on the toolbar
     */
    private fun showToolbarLoader() {
        with(binding.toolbar) {
            menu.findItem(R.id.save).setActionView(R.layout.toolbar_loader)
        }
    }
}