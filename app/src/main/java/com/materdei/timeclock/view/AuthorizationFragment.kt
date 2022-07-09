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
import com.materdei.timeclock.dto.RegisterDetails
import com.materdei.timeclock.security.BiometricAuthentication
import com.materdei.timeclock.utils.Constants.Companion.BIOMETRIC_NEGATIVE_BUTTON
import com.materdei.timeclock.utils.Constants.Companion.BIOMETRIC_SUBTITLE
import com.materdei.timeclock.utils.Constants.Companion.BIOMETRIC_TITLE
import com.materdei.timeclock.utils.PunchCard
import com.materdei.timeclock.viewmodels.DoItViewModel
import com.materdei.timeclock.viewmodels.RegisterViewModel
import java.text.DecimalFormat
import java.time.LocalDateTime

class AuthorizationFragment : Fragment() {

    private lateinit var binding: FragmentAuthorizationBinding
    private lateinit var biometricAuthentication : BiometricAuthentication
    private lateinit var doIt: DoItViewModel
    private lateinit var registerDetails: RegisterDetails
    private lateinit var registerViewModel: RegisterViewModel

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

        registerDetails = prepareData()
        updateUI()

        doIt = ViewModelProvider(this)[DoItViewModel::class.java]
        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        biometricAuthentication = BiometricAuthentication(this)

        binding.agreeBtn.setOnClickListener{
            biometricAuthentication.start(
                BIOMETRIC_TITLE,
                BIOMETRIC_SUBTITLE,
                BIOMETRIC_NEGATIVE_BUTTON
            )
        }

        binding.cancelBtn.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_authorizationFragment_to_homeFragment)
        }

        doIt.canDoIt.observe(viewLifecycleOwner) { flag ->
            if (flag) {
                doIt.disengage()
                registerViewModel.putRegister(registerDetails)
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_authorizationFragment_to_homeFragment)
            }
        }

        return binding.root
    }

    private fun prepareData(): RegisterDetails{

        val punch = if(AuthorizationFragmentArgs.fromBundle(arguments!!).isPunchIn)
            PunchCard.IN.value else PunchCard.OUT.value

        val date = LocalDateTime.now().let {
            val mFormat = DecimalFormat("00")
            "${it.year}-${mFormat.format(it.month.value)}-${mFormat.format(it.dayOfMonth)}"
        }

        val time = LocalDateTime.now().let {
            val mFormat = DecimalFormat("00")
            "${mFormat.format(it.hour)}-${mFormat.format(it.minute)}"
        }


        return RegisterDetails(date,time,punch)
    }

    private fun updateUI(){
        binding.titleTextView.text =
            "Registrando ${registerDetails.punch}"

        binding.timeTextView.text =
            "${registerDetails.date} às ${registerDetails.time.replace("-",":")}"
    }
}