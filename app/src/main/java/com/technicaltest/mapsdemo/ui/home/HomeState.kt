package com.technicaltest.mapsdemo.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import com.google.android.material.navigation.NavigationView
import com.technicaltest.mapsdemo.R
import com.technicaltest.mapsdemo.common.PermissionRequester
import com.technicaltest.mapsdemo.common.dialog.DialogFragmentLauncher
import com.technicaltest.mapsdemo.common.dialog.InfoDialog
import com.technicaltest.mapsdemo.ui.editUser.EditUserActivity
import com.technicaltest.mapsdemo.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HomeState (
    private val context: Context,
    private val drawerLayout: DrawerLayout,
    private val navView: NavigationView,
    private val viewModel: HomeViewModel,
    private val scope: CoroutineScope,
    private val locationPermissionRequester: PermissionRequester,
    private val dialogLauncher: DialogFragmentLauncher
    ) {

    fun setupNavigation(toggle: ActionBarDrawerToggle) {
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            handleMenuSelection(menuItem)
            drawerLayout.closeDrawer(navView)
            true
        }
    }

    private fun handleMenuSelection(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.nav_log_out -> {
                viewModel.signOut()
            }
            R.id.nav_user_edit -> {
                viewModel.navigateToEditUser()
            }
        }
    }

    fun navigateToLogin() =
        context.startActivity(
            Intent(context, LoginActivity::class.java)
        )

    fun navigateToEditUser(email: String, password: String) {
        val intent = Intent(context, EditUserActivity::class.java).apply {
            putExtra("email", email)
            putExtra("password", password)
        }
        context.startActivity(intent)
    }

    fun requestLocationPermission(afterRequest: (Boolean) -> Unit) {
        scope.launch {
            val result = locationPermissionRequester.request()
            afterRequest(result)
        }
    }

    fun showLocationUpdateDialog(latitude: Double, longitude: Double) {
        val infoDialog = InfoDialog.create(
            title = "Actualización de Ubicación",
            description = "Latitud: $latitude\nLongitud: $longitude",
            isDialogCancelable = true,
            positiveAction = InfoDialog.Action("OK") {
                it.dismiss()
            }
        )

        dialogLauncher.show(infoDialog, context as FragmentActivity)
    }


    fun showPermissionRequiredDialog() {
        val infoDialog = InfoDialog.create(
            title = "Permisos requeridos",
            description = "Para poder mostrar tu ubicación actual, necesitamos acceso a tu ubicación. Por favor, concede los permisos en la configuración.",
            isDialogCancelable = false,
            positiveAction = InfoDialog.Action("Configurar") {
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
                it.dismiss()
            },
            negativeAction = InfoDialog.Action("Cancelar") {
                it.dismiss()
            }
        )

        dialogLauncher.show(infoDialog, context as FragmentActivity)
    }
}
