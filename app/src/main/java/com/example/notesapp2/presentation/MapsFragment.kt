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
import java.util.concurrent.TimeUnit.*

const val TAG = "TAG"
class MapsFragment : Fragment() {
    private lateinit var map: GoogleMap
    private var listOfLatLng = arrayListOf<LatLng>()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */


        with(googleMap) {

            val task = fusedLocationProviderClient.lastLocation
            task.addOnFailureListener {
                Log.d("error", ": $it")
            }
            task.addOnSuccessListener {
                if (it != null) {
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
            map = googleMap
        }
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
        createLocationRequest()
        createLocationCallback()
        createFusedProvider()
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

    private fun createLocationCallback() {

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                saveLocation(locationResult)
//                Log.d("TAG", "Thread :${Thread.currentThread().name} ")
//                Log.d(
//                    "TAG",
//                    "onLocationResult: ${locationResult.lastLocation.longitude} ${locationResult.lastLocation.latitude}"
//                )

            }
        }
    }

    private fun saveLocation(locationResult: LocationResult) {
        map.clear()
        val lat = locationResult.lastLocation.latitude
        val lng = locationResult.lastLocation.longitude
        val timestamp = locationResult.lastLocation.time
        val latLng = LatLng(lat, lng)
        listOfLatLng.add(latLng)
        val polyline = PolylineOptions().color(Color.BLUE).width(5f)
        for (i in listOfLatLng.indices) {
            polyline.add(listOfLatLng[i])
        }
        map.addPolyline(polyline)
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        map.addMarker(MarkerOptions().position(latLng))
    }

    private fun checkPermissions() {

    }

    override fun onDestroyView() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback).addOnCompleteListener {
            Log.d("end", "onDestroyView: $fusedLocationProviderClient")
        }
        super.onDestroyView()
    }


    private companion object {
        const val MAX_WAIT_TIME = 2L
        const val FASTEST_INTERVAL = 30L
        const val INTERVAL = 100L
        const val MINIMUM_DISTANCE = 1f
    }

}