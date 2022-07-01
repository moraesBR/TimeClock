package com.materdei.timeclock.viewmodels

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import java.util.*

class LocationViewModel(): ViewModel() {

    private var _location = MutableLiveData<Location>()
    val location: LiveData<Location>
        get() = _location

    private var _canRegister = MutableLiveData<Boolean>()
    val canRegister: LiveData<Boolean>
        get() = _canRegister

    init {
        _location.value = Location(LocationManager.GPS_PROVIDER)
        _location.value!!.latitude = -1.3566939
        _location.value!!.longitude = -48.3934582
        _canRegister.value = false
    }

    @SuppressLint("MissingPermission")
    fun updateLocation(fusedLocationProviderClient: FusedLocationProviderClient){
        val locationRequest: LocationRequest = LocationRequest.create()
        locationRequest.priority = Priority.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2

        val locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                _location.value = p0.lastLocation!!
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback,Looper.myLooper()
        )
    }

    fun canPunchCard(latRef: Double, lonRef: Double, maxDistance: Float = 10.0F){
        val ref = Location(LocationManager.GPS_PROVIDER)
        ref.latitude = latRef
        ref.longitude = lonRef

        val distance = _location.value!!.distanceTo(ref)

        _canRegister.value =  distance <= maxDistance
    }
}