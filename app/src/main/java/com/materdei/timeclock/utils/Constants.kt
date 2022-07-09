package com.materdei.timeclock.utils

import java.util.*

class Constants {
    companion object{
        val BIOMETRIC_KEY = UUID.randomUUID().toString()
        val BIOMETRIC_TITLE = "Biometric login for my app"
        val BIOMETRIC_SUBTITLE = "Log in using your biometric credential"
        val BIOMETRIC_NEGATIVE_BUTTON = "Use account password"
        val LOCATION_MAX_DISTANCE = 10.0F
        val LOCATION_PERMISSION_REQUEST_CODE = 2022
        val LOCATION_REFERENCE_LAT = -1.3566939  // My Home
        val LOCATION_REFERENCE_LON = -48.3934582  // My Home
        // val REFERENCE_LAT = -1.431333  // Mater Dei
        // val REFERENCE_LON = -48.475374 // Mater Dei
        val LOCATION_REQUIREMENT = "Turn on your Internet or/and GPS, please"
        val LOCATION_TIME: Long = 60000
        val LOCATION_TOO_DISTANCE = "Too distance from Mater Dei"
        val LOGIN_ERRO_MSG = "Please, you must inform email and password correctly!"
        val REGISTERVIEWMODEL_ROOT_COLLECTION = "timeclock"
        val REGISTERVIEWMODEL_REGISTERS_COLLECTION = "registers"
        val SHAREDPREFERENCES = "sharedpreferences mater dei"
        val SHAREDPREFERENCES_LOGIN = "LOGIN_DATA"
        val SHAREDPREFERENCES_LOGIN_EMAIL = "email"
        val SHAREDPREFERENCES_LOGIN_PASSWORD = "password"
        val SHAREDPREFERENCES_LOGIN_SAVE = "save login"
    }
}