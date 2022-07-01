package com.materdei.timeclock

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.materdei.timeclock.databinding.ActivityMainBinding
import com.materdei.timeclock.viewmodels.LocationViewModel
import com.materdei.timeclock.viewmodels.PermissionHandlerAndroid


class MainActivity : AppCompatActivity() {


    private val permissionId = 1000
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var permissionViewModel: PermissionHandlerAndroid
    private lateinit var locationViewModel: LocationViewModel
    private val latRef: Double = -1.3566939
    private val lonRef: Double = -48.3934582
    /*private val latRef: Double = -1.431333
    private val lonRef: Double = -48.475374*/

    private lateinit var binding: ActivityMainBinding
    /* Coordination
    my house: canPunchCard(-1.3566939,-48.3934582)
    mater dei: canPunchCard(-1.431333,-48.475374)
    */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        supportActionBar?.hide()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        permissionViewModel = ViewModelProvider(this).get(PermissionHandlerAndroid::class.java)
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)

        if(!permissionViewModel.checkHasPermission(this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION))){
            permissionViewModel.requestPermission(this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION),permissionId)
        }
    }

    /*override fun onStart() {
        super.onStart()
        if (!permissionViewModel.checkLocationEnabled(this)) {
            Toast.makeText(this,"Please Enable your Location service",Toast.LENGTH_LONG).show()
            permissionViewModel.requestLocation(this)
        }
    }*/

    override fun onResume() {
        super.onResume()

        permissionViewModel.hasPermission.observe(this) { isPermitted ->
            if (isPermitted) {
                /*permissionViewModel.gpsEnabled.observe(this) { isEnabled ->
                    if (isEnabled) {
                        locationViewModel.updateLocation(fusedLocationProviderClient)
                    }
                }*/
                locationViewModel.updateLocation(fusedLocationProviderClient)
            }
        }

        locationViewModel.location.observe(this) { location ->
            if (location != null)
                locationViewModel.canPunchCard(latRef, lonRef)
        }

        locationViewModel.canRegister.observe(this) {
            if(it) {
                binding.btnFingerprint.visibility = View.VISIBLE
                binding.warningTextView.visibility = View.GONE
                binding.btnFingerprint.setOnClickListener{
                    val intent = Intent(this,HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            else {
                binding.warningTextView.visibility = View.VISIBLE
                binding.btnFingerprint.visibility = View.GONE
            }
        }
    }

}