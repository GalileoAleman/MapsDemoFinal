package com.technicaltest.mapsdemo.ui.login

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.technicaltest.mapsdemo.R
import com.technicaltest.mapsdemo.common.*
import com.technicaltest.mapsdemo.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginState: LoginState
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginState = LoginState(this, loginViewModel, binding)

        initListeners()

        lifecycleScope.launch{
            loginViewModel.state.collect{
                binding.updateUi(it)
            }
        }
    }

    private fun initListeners() {
        binding.etEmail.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.etEmail.onTextChanged { loginState.onFieldChanged() }

        binding.etPassword.loseFocusAfterAction(EditorInfo.IME_ACTION_DONE)
        binding.etPassword.setOnFocusChangeListener { _, hasFocus -> loginState.onFieldChanged(hasFocus) }
        binding.etPassword.onTextChanged { loginState.onFieldChanged() }

        binding.btnLogin.setOnClickListener {
            it.dismissKeyboard()
            loginViewModel.onLoginSelected(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }
    }


    private fun ActivityLoginBinding.updateUi(viewState : LoginViewModel.LoginViewUiState){
        pbLoading.isVisible = viewState.isLoading

        tilEmail.error =
            if (viewState.isValidUser) null else getString(R.string.login_error_mail)
        tilPassword.error =
            if (viewState.isValidPassword) null else getString(R.string.login_error_password)

        if (viewState.loginSuccess) {
            loginState.navigateToHome()
            finishAffinity()
        }
    }
}