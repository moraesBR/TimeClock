package com.materdei.timeclock.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.materdei.timeclock.R
import com.materdei.timeclock.databinding.FragmentHomeBinding
import com.materdei.timeclock.viewmodels.LocationPermission
import com.materdei.timeclock.viewmodels.LocationViewModel

const val LOCATION_PERMISSION_REQUEST_CODE = 2022

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var locationPermission: LocationPermission

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )

        locationPermission = LocationPermission(context!!)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        prepRequestLocationUpdate()
    }

    private fun prepRequestLocationUpdate() {
        locationPermission.requestLocation { isAllowed ->
            if(isAllowed){
                requestLocationUpdate()
            }
            else{
                val permissionRequest = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                ActivityCompat.requestPermissions(this.requireActivity(),permissionRequest,LOCATION_PERMISSION_REQUEST_CODE)
            }
        }
    }

    private fun requestLocationUpdate() {
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        locationViewModel.getLocationLiveData().observe(viewLifecycleOwner) {
            Log.i("REQ","ok3")
            Toast.makeText(
                context,
                "Lat ${it.latitude}; Lon ${it.longitude}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.i("TESTE","${LOCATION_PERMISSION_REQUEST_CODE}")
        when (requestCode){
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    locationPermission.requestLocation {
                        requestLocationUpdate()
                    }
                }
                else{
                    Toast.makeText(context,
                        "Unable to check your location without permission", Toast.LENGTH_LONG)
                        .show()
                }
            }
            else -> {
                Toast.makeText(context,
                    "No permission", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}