package com.cornershop.counterstest.features.create


import android.os.Bundle
import androidx.activity.viewModels
import com.cornershop.counterstest.R
import com.cornershop.counterstest.core.BaseActivity
import com.cornershop.counterstest.databinding.ActivityCreateBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CreateActivity : BaseActivity() {

    private val viewModel: CreateViewModel by viewModels()

    private lateinit var binding: ActivityCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureToolbar()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down)
    }

    private fun configureToolbar() {
        binding.toolbar?.let { toolbar ->
            toolbar.title = getString(R.string.create_counter)
            toolbar.inflateMenu(R.menu.toolbar_create)
            toolbar.setNavigationIcon(R.drawable.ic_close)
            toolbar.setNavigationOnClickListener { finish() }
            toolbar.setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == R.id.save) {
                    viewModel.createCounter()
                }
                return@setOnMenuItemClickListener true
            }
        }
    }
}