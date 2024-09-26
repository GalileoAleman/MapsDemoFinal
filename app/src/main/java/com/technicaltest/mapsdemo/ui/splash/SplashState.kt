package com.technicaltest.mapsdemo.ui.splash

import android.app.Activity
import android.content.Intent
import com.technicaltest.mapsdemo.ui.home.HomeActivity
import com.technicaltest.mapsdemo.ui.login.LoginActivity

class SplashState(private val activity: Activity) {

    fun navigateTo(userLoged: Boolean) {
        val intent = if (userLoged) {
            Intent(activity, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        } else {
            Intent(activity, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
        activity.startActivity(intent)
        activity.finish()
    }
}
