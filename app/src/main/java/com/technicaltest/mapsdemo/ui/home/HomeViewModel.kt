package com.technicaltest.mapsdemo.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.technicaltest.domain.LocationResult
import com.technicaltest.domain.User
import com.technicaltest.mapsdemo.ui.editUser.EditUserViewModel.EditUserViewUiState
import com.technicaltest.usecases.FindLocationUseCase
import com.technicaltest.usecases.FindUserUseCase
import com.technicaltest.usecases.SignoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val signoutUseCase: SignoutUseCase,
                                        private val findUserUseCase: FindUserUseCase,
                                        private val findLocationUseCase: FindLocationUseCase
    ) : ViewModel(){

    data class HomeViewUiState(
        val signOut : Boolean = false,
        val navigateToEditUser : Boolean = false,
        val user: User? = null,
        val currentLocation: LatLng? = null,
        val errorMessage: String? = null,
        )

    private val _state = MutableStateFlow(HomeViewUiState())
    val state : StateFlow<HomeViewUiState> = _state.asStateFlow()

    init{
        viewModelScope.launch {
            findUserUseCase(1).collect{
                Log.d("HomeViewModel", "user: $it")
                _state.value = HomeViewUiState(user = it)
            }
        }
    }

    fun signOut(){
        viewModelScope.launch {
            signoutUseCase()
            _state.value = HomeViewUiState(signOut = true)
        }
    }

    fun navigateToEditUser() {
        _state.value = _state.value.copy(navigateToEditUser = true)
    }

    fun fetchCurrentLocation() {
        viewModelScope.launch {
            when (val result = findLocationUseCase()) {
                is LocationResult.Success -> {
                    val latLng = LatLng(result.latitude, result.longitude)
                    _state.value = _state.value.copy(currentLocation = latLng)
                }
                is LocationResult.Error -> {
                    _state.value = _state.value.copy(currentLocation = null, errorMessage = result.message)
                }
            }
        }
    }

    fun onCurrentLocationChanged() {
        _state.value = _state.value.copy(currentLocation = null)
    }
}
