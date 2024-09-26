package com.technicaltest.mapsdemo.ui.editUser

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.technicaltest.domain.Posts
import com.technicaltest.domain.User
import com.technicaltest.mapsdemo.common.isValidPassword
import com.technicaltest.mapsdemo.common.isValidUser
import com.technicaltest.usecases.EditUserUseCase
import com.technicaltest.usecases.GetPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditUserViewModel @Inject constructor(
    private val editUserUseCase: EditUserUseCase,
    private val getPostsUseCase: GetPostsUseCase
) : ViewModel(){

    data class EditUserViewUiState(
        val isLoading: Boolean = false,
        val isValidUser: Boolean = true,
        val isValidPassword: Boolean = true,
        val editUserSuccess: Boolean = false,
        val showError: String? = null,
        val posts: List<Posts>? = null
    )

    private val _state = MutableStateFlow(EditUserViewUiState())
    val state : StateFlow<EditUserViewUiState> = _state.asStateFlow()

    fun onEditUserSelected(email: String, password: String) {
        val isValidUser = email.isValidUser()
        val isValidPassword = password.isValidPassword()

        if (isValidUser && isValidPassword) {
            editUser(User(email, password, true))
        } else {
            _state.value = EditUserViewUiState(
                isValidUser = isValidUser,
                isValidPassword = isValidPassword)
        }
    }

    fun editUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main){_state.value = EditUserViewUiState(isLoading = true)}

            val deferredEditUser = async { editUserUseCase(user) }
            val deferredGetPosts = async { getPostsUseCase() }

            val editUserResult = deferredEditUser.await()
            val postsResult = deferredGetPosts.await()

            Log.d("EditUserViewModel", "editUser: $editUserResult")
            Log.d("EditUserViewModel", "postsResult: $postsResult")

            if (editUserResult.success) {
                withContext(Dispatchers.Main){_state.value = EditUserViewUiState(editUserSuccess = true)}
            } else {
                withContext(Dispatchers.Main){_state.value = EditUserViewUiState(showError = editUserResult.errorMsg)}
            }

            if (postsResult.success) {
                withContext(Dispatchers.Main){_state.value = EditUserViewUiState(posts = postsResult.posts)}
            }
            else
                withContext(Dispatchers.Main){_state.value = EditUserViewUiState(posts = null)}

            withContext(Dispatchers.Main){_state.value = EditUserViewUiState(isLoading = false)}
        }
    }

    fun onFieldsChanged() {
        _state.value = EditUserViewUiState()
    }
}
