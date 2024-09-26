package com.technicaltest.mapsdemo.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.technicaltest.mapsdemo.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var splashState: SplashState
    private val viewModel : SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        splashState = SplashState(this)

        lifecycleScope.launch {
            viewModel.state.collect{
                Log.d("SplashActivity", "userLoged: " + it.userLoged)
                splashState.navigateTo(it.userLoged)
            }
        }
    }
}
