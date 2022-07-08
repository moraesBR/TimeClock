package com.materdei.timeclock.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.materdei.timeclock.R
import com.materdei.timeclock.databinding.FragmentMainBinding
import com.materdei.timeclock.dto.AuthState.AuthError
import com.materdei.timeclock.dto.DataSharedPreferences
import com.materdei.timeclock.dto.FragmentsID
import com.materdei.timeclock.dto.User
import com.materdei.timeclock.security.FirebaseAuthentication
import com.materdei.timeclock.viewmodels.AppSharedPreferences

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var firebaseAuthentication: FirebaseAuthentication


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )

        firebaseAuthentication = FirebaseAuthentication()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        with(binding) {
            val savedPreferences = AppSharedPreferences.restore(requireContext(),FragmentsID.MAIN)
                as DataSharedPreferences.MainPreferences

            user = savedPreferences.user
            saveLogin.isChecked = savedPreferences.isChecked

            loginButton.setOnClickListener {

                saveLogin()

                it.visibility = View.GONE
                waitingLogin.visibility = View.VISIBLE

                firebaseAuthentication.handleSignIn(user!!){ isSuccess ->
                    loginButton.visibility = View.VISIBLE
                    waitingLogin.visibility = View.GONE
                    if (isSuccess)
                    {
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_mainFragment_to_homeFragment)
                    }
                    else
                    {
                        val errorMessage = (firebaseAuthentication.authState as AuthError).msg
                        Toast.makeText(context,errorMessage,Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun saveLogin(){
        if(binding.saveLogin.isChecked){
            AppSharedPreferences.save(
                requireContext(),
                FragmentsID.MAIN,
                DataSharedPreferences.MainPreferences(binding.user!!,true)
            )
        } else{
            AppSharedPreferences.save(
                requireContext(),
                FragmentsID.MAIN,
                DataSharedPreferences.MainPreferences(User("",""),false)
            )
        }
    }
}