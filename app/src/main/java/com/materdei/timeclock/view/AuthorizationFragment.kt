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
import com.materdei.timeclock.security.BiometricAuthentication
import com.materdei.timeclock.utils.Constants.Companion.BIOMETRIC_NEGATIVE_BUTTON
import com.materdei.timeclock.utils.Constants.Companion.BIOMETRIC_SUBTITLE
import com.materdei.timeclock.utils.Constants.Companion.BIOMETRIC_TITLE
import com.materdei.timeclock.viewmodels.DoItViewModel

class AuthorizationFragment : Fragment() {

    private lateinit var binding: FragmentAuthorizationBinding
    private lateinit var biometricAuthetication : BiometricAuthentication
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

        biometricAuthetication = BiometricAuthentication(this)

        binding.agreeBtn.setOnClickListener{
            biometricAuthetication.start(
                BIOMETRIC_TITLE,
                BIOMETRIC_SUBTITLE,
                BIOMETRIC_NEGATIVE_BUTTON
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