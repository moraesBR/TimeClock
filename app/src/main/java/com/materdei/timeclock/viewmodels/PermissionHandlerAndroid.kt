package com.materdei.timeclock.viewmodels

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PermissionHandlerAndroid : ViewModel(), PermissionsHandler {

    private var _hasPermission = MutableLiveData(false)
    val hasPermission: LiveData<Boolean>
        get() = _hasPermission

    private var _gpsEnabled = MutableLiveData(false)
    val gpsEnabled: LiveData<Boolean>
        get() = _gpsEnabled

    override fun checkHasPermission(activity: AppCompatActivity, permissions: Array<String>): Boolean {
        _hasPermission.value = permissions.asIterable().all {
            ContextCompat.checkSelfPermission(activity,it) == PackageManager.PERMISSION_GRANTED
        }
        return hasPermission.value as Boolean
    }

    override fun requestPermission(
        activity: AppCompatActivity,
        permissions: Array<out String>,
        requestCode: Int
    ) {
        ActivityCompat.requestPermissions(activity,permissions,requestCode)
    }

    override fun checkLocationEnabled(activity: AppCompatActivity): Boolean{
        val locationManager: LocationManager =
            activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        _gpsEnabled.value = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gpsEnabled.value as Boolean
    }

    override fun requestLocation(activity: AppCompatActivity) {
        val alertDialog: AlertDialog = activity.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage("Your GPS seems to be disabled, do you want to enable it?")
                setCancelable(false)
                setPositiveButton("Yes"
                ) { _, _ ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    it.startActivity(intent)
                }
                setNegativeButton("No"
                ) { _, _ ->
                    it.finish()
                }
            }
            builder.create()
        }
        alertDialog.show()
    }
}