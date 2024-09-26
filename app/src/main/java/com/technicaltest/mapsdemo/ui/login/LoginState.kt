package com.technicaltest.mapsdemo.ui.login

import android.content.Context
import android.content.Intent
import com.technicaltest.mapsdemo.databinding.ActivityLoginBinding
import com.technicaltest.mapsdemo.ui.home.HomeActivity

class LoginState(
    private val context: Context,
    private val loginViewModel: LoginViewModel,
    private val binding: ActivityLoginBinding
) {

    fun onFieldChanged(hasFocus: Boolean = false) {
        if (!hasFocus) {
            loginViewModel.onFieldsChanged(
                email = binding.etEmail.text.toString().trim(),
                password = binding.etPassword.text.toString().trim()
            )
        }
    }

    fun navigateToHome() =
        context.startActivity(Intent(context, HomeActivity::class.java))
}
