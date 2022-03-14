package com.ametszc.technical_test.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.ametszc.technical_test.R
import com.ametszc.technical_test.adapters.HomeNavigationStateAdapter
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import com.google.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceLikelihood
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener,
    EasyPermissions.PermissionCallbacks,
    NearestPlacesFragment.OnPlaceSelected {

    // Use fields to define the data types to return.
    val placeFields: List<Place.Field> = listOf(Place.Field.NAME)

    // Use the builder to create a FindCurrentPlaceRequest.
    val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

    lateinit var mFusedLocationClient: FusedLocationProviderClient

    private val homeStatePageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            viewPager.currentItem = position
            when (position) {
                0 -> bottomAppBar.menu.findItem(R.id.places_list).isChecked = true
                1 -> bottomAppBar.menu.findItem(R.id.favorite_places).isChecked = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViews()
        setListeners()
        verifyPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager.unregisterOnPageChangeCallback(homeStatePageChangeCallback)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.places_list -> {
                viewPager.currentItem = 0
                true
            }
            R.id.favorite_places -> {
                viewPager.currentItem = 1
                true
            }
            else -> false
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(this).build().show()
        } else {
            verifyPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show()
    }

    private fun setViews() {
        viewPager.adapter = HomeNavigationStateAdapter(this)
        viewPager.registerOnPageChangeCallback(homeStatePageChangeCallback)
    }

    private fun setListeners() {
        bottomAppBar.setOnNavigationItemSelectedListener(this@MainActivity)
    }

    private fun verifyPermissions() {
        if (EasyPermissions.hasPermissions(this, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)) {
            //getPlaces()
            getPosition()
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.permission_location_rationale_message),
                REQUEST_CODE_LOCATION,
                ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun getPlaces() {
        loader.visibility = View.VISIBLE
        Places.initialize(applicationContext, "AIzaSyA1_M1tyw474M2xQpk74v5soZNfjKEp-GU")
        // Create a new PlacesClient instance
        val placesClient = Places.createClient(this)
        val placeResponse = placesClient.findCurrentPlace(request)
        placeResponse.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val response = task.result
                for (placeLikelihood: PlaceLikelihood in response?.placeLikelihoods ?: emptyList()) {
                    Log.i(
                        TAG,
                        "Place '${placeLikelihood.place.name}' has likelihood: ${placeLikelihood.likelihood}"
                    )
                }
            } else {
                val exception = task.exception
                if (exception is ApiException) {
                    Log.e(TAG, "Place not found: ${exception.statusCode}")
                }
            }
            loader.visibility = View.INVISIBLE
        }
    }

    @SuppressLint("MissingPermission")
    private fun getPosition() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
            var location: Location? = task.result
            if (location == null) {
                requestNewLocationData()
            } else {
                locationCompanion = LatLng(location.latitude, location.longitude)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            locationCompanion = LatLng(mLastLocation.latitude, mLastLocation.longitude)
        }
    }

    companion object {
        const val TAG = "PLACES::"
        val REQUEST_CODE_LOCATION = 1000
        var locationCompanion: LatLng? = null
    }

    override fun openPlaceDetail(place: com.ametszc.domain.Place) {
        TODO("Not yet implemented")
    }
}