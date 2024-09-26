package com.technicaltest.mapsdemo.ui.editUser

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.technicaltest.mapsdemo.R
import com.technicaltest.mapsdemo.common.dismissKeyboard
import com.technicaltest.mapsdemo.common.loseFocusAfterAction
import com.technicaltest.mapsdemo.common.onTextChanged
import com.technicaltest.mapsdemo.common.toast
import com.technicaltest.mapsdemo.databinding.ActivityEditUserBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditUserActivity : AppCompatActivity() {

    private val editUserViewModel: EditUserViewModel by viewModels()
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var binding: ActivityEditUserBinding
    private lateinit var editUserState: EditUserState


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            email = it.getStringExtra("email") ?: ""
            password = it.getStringExtra("password") ?: ""
        }

        editUserState = EditUserState(this, editUserViewModel)

        binding.etEmail.setText(email)
        binding.etPassword.setText(password)
        initListeners()

        lifecycleScope.launch{
            editUserViewModel.state.collect{
                binding.updateUi(it)
            }
        }
    }

    private fun initListeners() {
        binding.etEmail.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.etEmail.onTextChanged { editUserState.onFieldChanged() }

        binding.etPassword.loseFocusAfterAction(EditorInfo.IME_ACTION_DONE)
        binding.etPassword.setOnFocusChangeListener { _, hasFocus -> editUserState.onFieldChanged(hasFocus) }
        binding.etPassword.onTextChanged { editUserState.onFieldChanged() }

        binding.btnEdit.setOnClickListener {
            it.dismissKeyboard()
            editUserViewModel.onEditUserSelected(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }
    }

    fun ActivityEditUserBinding.updateUi(viewState: EditUserViewModel.EditUserViewUiState){
        pbLoading.isVisible = viewState.isLoading

        tilEmail.error =
            if (viewState.isValidUser) null else getString(R.string.login_error_mail)
        tilPassword.error =
            if (viewState.isValidPassword) null else getString(R.string.login_error_password)

        if (viewState.editUserSuccess) {
            editUserState.navigateToHome()
        }


        viewState.posts?.let { applicationContext.toast("Numero de posts: " + it.size) }
    }
}
