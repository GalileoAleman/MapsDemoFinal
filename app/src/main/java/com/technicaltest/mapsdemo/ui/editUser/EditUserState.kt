package com.technicaltest.mapsdemo.ui.editUser

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.technicaltest.mapsdemo.ui.home.HomeActivity

class EditUserState(
    private val context: Context,
    private val editUserViewModel: EditUserViewModel
) {

    fun onFieldChanged(hasFocus: Boolean = false) {
        if (!hasFocus) {
            editUserViewModel.onFieldsChanged()
        }
    }

    fun navigateToHome() {
        val intent = Intent(context, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)

        if (context is Activity) {
            context.finish()
        }
    }
}
