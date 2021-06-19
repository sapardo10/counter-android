package com.cornershop.counterstest.welcome

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cornershop.counterstest.R
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
            viewModel.test()
        }
    }
}
