package com.example.notesapp2.presentation.utils

import android.app.Service
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.LocationManager
import android.os.IBinder
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class TestLocation : Service() {

    private val scope = CoroutineScope(Dispatchers.Main)

    private val locationManager =
        getSystemService(LOCATION_SERVICE) as LocationManager

    override fun onCreate() {
        super.onCreate()

    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

}