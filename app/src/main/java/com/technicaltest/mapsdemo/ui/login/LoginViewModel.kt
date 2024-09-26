package com.technicaltest.mapsdemo.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.technicaltest.domain.User
import com.technicaltest.mapsdemo.common.*
import com.technicaltest.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel(){


    data class LoginViewUiState(
        val isLoading: Boolean = false,
        val isValidUser: Boolean = true,
        val isValidPassword: Boolean = true,
        val loginSuccess: Boolean = false,
        val showError: String? = null
    )

    private val _state = MutableStateFlow(LoginViewUiState())
    val state : StateFlow<LoginViewUiState> = _state.asStateFlow()

    fun onLoginSelected(email: String, password: String) {
        val isValidUser = email.isValidUser()
        val isValidPassword = password.isValidPassword()

        if (isValidUser && isValidPassword) {
            loginUser(User(email, password, true))
        } else {
            _state.value = LoginViewUiState(
                isValidUser = isValidUser,
                isValidPassword = isValidPassword)
        }
    }

    fun loginUser(user: User) {
        viewModelScope.launch {
            _state.value = LoginViewUiState(isLoading = true)

            val result = loginUseCase(user)

            if (result.success) {
                _state.value = LoginViewUiState(loginSuccess = true, isLoading = false)
            } else {
                _state.value = LoginViewUiState(isLoading = false, showError = result.errorMsg)
            }
        }
    }

    fun onFieldsChanged(email: String, password: String) {
        _state.value = LoginViewUiState(
            isValidUser = email.isValidUser(),
            isValidPassword = password.isValidPassword()
        )
    }

}
