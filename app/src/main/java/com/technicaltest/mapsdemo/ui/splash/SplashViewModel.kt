package com.technicaltest.mapsdemo.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.technicaltest.usecases.CurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor (private val currentUserUseCase: CurrentUserUseCase) : ViewModel() {

    data class SplashViewUiState(
        val userLoged : Boolean = false
    )

    private val _state = MutableStateFlow(SplashViewUiState())
    val state : StateFlow<SplashViewUiState> = _state.asStateFlow()

    init{
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch(Dispatchers.Main) {
            _state.value = SplashViewUiState(userLoged = currentUserUseCase())
        }
    }
}