package com.materdei.timeclock.security

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.materdei.timeclock.utils.Constants.Companion.LOCATION_REQUIREMENT

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
            Toast.makeText(context,LOCATION_REQUIREMENT,Toast.LENGTH_SHORT).show()
    }

}