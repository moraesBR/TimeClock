package com.materdei.timeclock.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.materdei.timeclock.R
import com.materdei.timeclock.databinding.FragmentAuthorizationBinding
import com.materdei.timeclock.security.BiometricAuthetication
import com.materdei.timeclock.viewmodels.DoItViewModel

class AuthorizationFragment : Fragment() {

    private lateinit var binding: FragmentAuthorizationBinding
    private lateinit var biometricAuthetication : BiometricAuthetication
    private lateinit var doIt: DoItViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_authorization,
            container,
            false
        )

        doIt = ViewModelProvider(this).get(DoItViewModel::class.java)

        biometricAuthetication = BiometricAuthetication(this)

        binding.agreeBtn.setOnClickListener{
            biometricAuthetication.start(
                "Biometric login for my app",
                "Log in using your biometric credential",
                "Use account password"
            )
        }

        binding.cancelBtn.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_authorizationFragment_to_homeFragment)
        }

        doIt.canDoIt.observe(viewLifecycleOwner, { flag ->
                if(flag){
                    doIt.disengage()
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_authorizationFragment_to_homeFragment)
                }
            }
        )

        return binding.root
    }

}