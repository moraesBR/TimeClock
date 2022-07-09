package com.materdei.timeclock.view

import android.Manifest
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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.materdei.timeclock.utils.Constants.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.materdei.timeclock.utils.Constants.Companion.LOCATION_TOO_DISTANCE
import com.materdei.timeclock.R
import com.materdei.timeclock.databinding.FragmentHomeBinding
import com.materdei.timeclock.security.LocationPermission
import com.materdei.timeclock.viewmodels.LocationViewModel
import com.materdei.timeclock.viewmodels.RegisterViewModel


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var locationPermission: LocationPermission
    private lateinit var registerViewModel: RegisterViewModel

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

        binding.registerRecyclerView.layoutManager = LinearLayoutManager(this.requireContext())

        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        registerViewModel.getRegisters()

        locationPermission = LocationPermission(context!!)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        registerViewModel.newRegisters()

        registerViewModel.dbRegisters.observe(viewLifecycleOwner){
            it.forEach { newData ->
                Log.i("FIRESTORE", newData.getKey())
            }
        }

        binding.punchInBtn.setOnClickListener{
            prepRequestLocationUpdate(true)
        }

        binding.punchOutBtn.setOnClickListener{
            prepRequestLocationUpdate(false)
        }
    }

    private fun prepRequestLocationUpdate(isPunchIn: Boolean) {
        locationPermission.requestLocation { isAllowed ->
            if(isAllowed){
                requestLocationUpdate(isPunchIn)
            }
            else{
                val permissionRequest = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                ActivityCompat.requestPermissions(this.requireActivity(),permissionRequest,LOCATION_PERMISSION_REQUEST_CODE)
            }
        }
    }

    private fun requestLocationUpdate(isPunchIn: Boolean) {
        locationViewModel = ViewModelProvider(this)[LocationViewModel::class.java]
        locationViewModel.getLocationLiveData().observe(viewLifecycleOwner) {
            if(it.isNear())
                //Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_authorizationFragment)
                Navigation.findNavController(binding.root)
                    .navigate(
                        HomeFragmentDirections
                            .actionHomeFragmentToAuthorizationFragment(isPunchIn)
                    )
            else
                Toast.makeText(
                    context,
                    LOCATION_TOO_DISTANCE,
                    Toast.LENGTH_SHORT
                ).show()
        }
    }
}