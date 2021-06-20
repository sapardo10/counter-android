package com.cornershop.counterstest.welcome

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.cornershop.counterstest.R
import com.cornershop.data.models.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {

    private val viewModel: WelcomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        ///TODO: delete this testing lines
        val image = findViewById<ImageView>(R.id.imageLogo)
        image.setOnClickListener {
            viewModel.onCreateCounterTapped()
        }

        val textDescription = findViewById<TextView>(R.id.textDescription)
        viewModel.counters.observe(this, {
            it?.let { list ->
                var text = ""
                list.forEach { counter -> text += "\n $counter" }
                textDescription.text = text
            }
        })

        viewModel.initViewModel()
    }
}
