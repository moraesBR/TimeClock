package com.materdei.timeclock.viewmodels

import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LiveData
import com.google.android.gms.location.*
import com.materdei.timeclock.utils.Constants.Companion.LOCATION_TIME
import com.materdei.timeclock.dto.LocationDetails

class LocationLiveData(context: Context) : LiveData<LocationDetails>(){

    private var fusedLocationClient: FusedLocationProviderClient = LocationServices
        .getFusedLocationProviderClient(context)

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            for(location in locationResult.locations){
                if(location != null) setLocationData(location)
            }
        }
    }

    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            priority = Priority.PRIORITY_HIGH_ACCURACY
            interval = LOCATION_TIME
            fastestInterval = LOCATION_TIME/4
        }
    }

    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onActive() {
        super.onActive()
        fusedLocationClient.lastLocation.addOnSuccessListener {
            location: Location? -> location.also {
                if(it != null) setLocationData(it)
            }
        }
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())
    }

    private fun setLocationData(location: Location){
        value = LocationDetails(location.latitude.toString(),location.longitude.toString())
    }
}