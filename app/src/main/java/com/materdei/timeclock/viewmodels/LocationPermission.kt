package com.materdei.timeclock.viewmodels

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.widget.Toast
import androidx.core.content.ContextCompat

class LocationPermission(private val context: Context) {

    private val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val deviceEnabled
    get() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    private val checkPermission
    get() = (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED)

    fun requestLocation(request:(isAllowed:Boolean) -> Unit ) {
        if(deviceEnabled)
            request(checkPermission)
        else
            Toast.makeText(context,"Turn on your Internet or/and GPS, please",Toast.LENGTH_SHORT).show()
    }

}