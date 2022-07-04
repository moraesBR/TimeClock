package com.materdei.timeclock.dto

import android.location.Location
import android.location.LocationManager
import com.materdei.timeclock.utils.Constants.Companion.MAX_DISTANCE
import com.materdei.timeclock.utils.Constants.Companion.REFERENCE_LAT
import com.materdei.timeclock.utils.Constants.Companion.REFERENCE_LON

data class LocationDetails (val latitude: String, val longitude: String){
    private val reference: Location = Location(LocationManager.GPS_PROVIDER).apply {
        latitude = REFERENCE_LAT
        longitude = REFERENCE_LON
    }

    private val maxDistance: Float = MAX_DISTANCE

    fun isNear(): Boolean{
        val local = Location(LocationManager.GPS_PROVIDER)
        local.latitude = this.latitude.toDouble()
        local.longitude = this.longitude.toDouble()

        return local.distanceTo(reference) <= maxDistance
    }
}