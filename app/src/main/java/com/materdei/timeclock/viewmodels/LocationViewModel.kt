package com.materdei.timeclock.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class LocationViewModel (app: Application) : AndroidViewModel (app) {

    private val locationLiveData = LocationLiveData(app)
    fun getLocationLiveData() = locationLiveData

}