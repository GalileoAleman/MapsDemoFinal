package com.technicaltest.mapsdemo.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.technicaltest.mapsdemo.R
import com.technicaltest.mapsdemo.common.PermissionRequester
import com.technicaltest.mapsdemo.common.dialog.DialogFragmentLauncher
import com.technicaltest.mapsdemo.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), OnMapReadyCallback {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityHomeBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var homeState: HomeState

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var currentMarker: Marker? = null

    @Inject
    lateinit var dialogLauncher: DialogFragmentLauncher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        val drawerLayout = binding.mainDrawerLayout
        val navView = binding.navigationView
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(this, drawerLayout, binding.toolbar, R.string.open, R.string.close)

        homeState = HomeState(this, drawerLayout, navView, homeViewModel,
            lifecycleScope, PermissionRequester(this, Manifest.permission.ACCESS_FINE_LOCATION),
            dialogLauncher
        )

        homeState.setupNavigation(toggle)

        val userNameView = navView.getHeaderView(0).findViewById<TextView>(R.id.user_name)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            homeViewModel.onCurrentLocationChanged()
            homeViewModel.fetchCurrentLocation()
        }

        lifecycleScope.launch {
            homeViewModel.state.collect{
                if(it.signOut) {
                    homeState.navigateToLogin()
                    finish()
                }

                if(it.navigateToEditUser) {
                    homeState.navigateToEditUser(it.user?.email ?: "", it.user?.password ?: "")
                }

                Log.d("HomeActivity", "user: ${it.user}")

                userNameView.text = it.user?.email

                if (it.currentLocation != null) {
                    getCurrentLocation(it.currentLocation)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        homeState.requestLocationPermission { isGranted ->
            if (isGranted) {
                mMap.isMyLocationEnabled = true
                homeViewModel.fetchCurrentLocation()
            } else {
                homeState.showPermissionRequiredDialog()
            }
        }
    }

    private fun getCurrentLocation(currentLocation: LatLng) {
        currentMarker?.let{
            it.remove()
            homeState.showLocationUpdateDialog(currentLocation.latitude, currentLocation.longitude)
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
        currentMarker = mMap.addMarker(MarkerOptions().position(currentLocation).title("Mi ubicación"))
        swipeRefreshLayout.isRefreshing = false
    }
}
