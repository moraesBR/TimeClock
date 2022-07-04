package com.materdei.timeclock.view

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.materdei.timeclock.Constants.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.materdei.timeclock.Constants.Companion.TOO_DISTANCE
import com.materdei.timeclock.R
import com.materdei.timeclock.databinding.FragmentHomeBinding
import com.materdei.timeclock.viewmodels.LocationPermission
import com.materdei.timeclock.viewmodels.LocationViewModel



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
        binding.punchInBtn.setOnClickListener{
            prepRequestLocationUpdate()
        }

        binding.punchOutBtn.setOnClickListener{
            prepRequestLocationUpdate()
        }
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
            if(it.isNear())
                Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_authorizationFragment)
            else
                Toast.makeText(
                    context,
                    TOO_DISTANCE,
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    /*@Deprecated("Deprecated in Java")
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
    }*/
}