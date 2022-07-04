package com.materdei.timeclock.utils

import java.util.*

class Constants {
    companion object{
        val REFERENCE_LAT = -1.3566939  // My Home
        val REFERENCE_LON = -48.3934582  // My Home
        // val REFERENCE_LAT = -1.431333  // Mater Dei
        // val REFERENCE_LON = -48.475374 // Mater Dei
        val MAX_DISTANCE = 10.0F
        val LOCATION_PERMISSION_REQUEST_CODE = 2022
        val TOO_DISTANCE = "Too distance from Mater Dei"
        val ONE_MINUTE: Long = 60000
        val KEY_BIOMETRIC = UUID.randomUUID().toString()
    }
}