package com.example.notesapp2.presentation

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.notesapp2.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationRequest.create
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit.*

const val TAG = "TAG"

class MapsFragment : Fragment() {
    private var flag = false
    private lateinit var map: GoogleMap
    private var listOfLatLng = arrayListOf<LatLng>()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        createLocationRequest()
        createLocationCallback()
        createFusedProvider()
        with(map) {
            val task = fusedLocationProviderClient.lastLocation
            task.addOnFailureListener {
                Log.d("error", ": $it")
            }
            task.addOnSuccessListener {
                if (it != null) {
                    Log.d(TAG, "lastLocation: ")
                    val currentLocation = LatLng(it.latitude, it.longitude)
                    animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
                }
            }
            isMyLocationEnabled = true
            with(uiSettings) {
                isCompassEnabled = true
                isZoomControlsEnabled = true
                isRotateGesturesEnabled = true
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,flag)
        super.onSaveInstanceState(outState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    @SuppressLint("MissingPermission")
    private fun createFusedProvider() {
        fusedLocationProviderClient = getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun createLocationRequest() {
        locationRequest = create().apply {
            interval = SECONDS.toMillis(INTERVAL)
//            fastestInterval = SECONDS.toMillis(FASTEST_INTERVAL)
//            maxWaitTime = MINUTES.toMillis(MAX_WAIT_TIME)
            priority = PRIORITY_HIGH_ACCURACY
            smallestDisplacement = MINIMUM_DISTANCE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        map.uiSettings.isMyLocationButtonEnabled = false
        Log.d(TAG, "onDestroy: 111")
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
//                for (location in locationResult.locations){
//                    Log.d(TAG, "onLocationResult: ${locationResult.locations.size}")
//                    Log.d(TAG, "onLocationResult: ${location.latitude} ${location.longitude}")
//                }
                saveLocation(locationResult)
            }
        }
    }

    private fun saveLocation(locationResult: LocationResult) {
        map.clear()
        val lastLocation = locationResult.lastLocation
        val lat = lastLocation.latitude
        val lng = lastLocation.longitude
        val timestamp = locationResult.lastLocation.time
        val latLng = LatLng(lat, lng)
        listOfLatLng.add(latLng)
        val polyline = PolylineOptions()
            .color(Color.BLUE)
            .width(POLYLINE_WIDTH)
        for (i in listOfLatLng.indices) {
            polyline.add(listOfLatLng[i])
        }
        map.addPolyline(polyline)
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    override fun onStop() {
        super.onStop()
        removeLocationUpdates()
        Log.d(TAG, "onStopReceiveUpdates: ")
    }

    private fun removeLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            .addOnCompleteListener {
                Log.d("end", "onDestroyView: $fusedLocationProviderClient")
            }
    }


    private companion object {
        const val REQUESTING_LOCATION_UPDATES_KEY = "request key"
        const val POLYLINE_WIDTH = 5f
        const val MAX_WAIT_TIME = 2L
        const val FASTEST_INTERVAL = 30L
        const val INTERVAL = 100L
        const val MINIMUM_DISTANCE = 1f
    }

}