package com.materdei.timeclock.view

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.materdei.timeclock.R
import com.materdei.timeclock.databinding.FragmentMainBinding
import com.materdei.timeclock.dto.User
import com.materdei.timeclock.security.FirebaseAuthentication
import com.materdei.timeclock.utils.Constants.Companion.LOGIN_MSG
import com.materdei.timeclock.utils.Constants.Companion.SHAREDPREFERENCES_LOGIN
import com.materdei.timeclock.utils.Constants.Companion.SHAREDPREFERENCES_LOGIN_EMAIL
import com.materdei.timeclock.utils.Constants.Companion.SHAREDPREFERENCES_LOGIN_PASSWORD
import com.materdei.timeclock.utils.Constants.Companion.SHAREDPREFERENCES_LOGIN_SAVE

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private var user: User = User("","")
    private lateinit var saveDataEditor: SharedPreferences.Editor
    private lateinit var sharedPreferences: SharedPreferences
    private var isChecked = true

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

        binding.user = user

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuthentication.connect()
    }

    override fun onResume() {
        super.onResume()

        restoreSharedPreferences()

        binding.loginButton.setOnClickListener{

            onCheckboxClicked(binding.saveLogin)

            FirebaseAuthentication.performAuthetication(binding.user!!){ isSucess ->
                invalidateAll()
                if (isSucess){
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_mainFragment_to_homeFragment)
                }
                else {
                    Toast.makeText(context,LOGIN_MSG,Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    fun onCheckboxClicked (view: View){
        if(view is CheckBox){
            val checked: Boolean = view.isChecked

            when(view.id) {
                R.id.saveLogin -> {
                    saveSharedPreference(checked)
                }
            }
        }
    }

    fun saveSharedPreference(flag: Boolean){
        saveDataEditor = requireActivity()
            .getSharedPreferences(SHAREDPREFERENCES_LOGIN,MODE_PRIVATE).edit()
        if (flag) {
            saveDataEditor.putString(SHAREDPREFERENCES_LOGIN_EMAIL,binding.user!!.email)
            saveDataEditor.putString(SHAREDPREFERENCES_LOGIN_PASSWORD,binding.user!!.password)
            saveDataEditor.putBoolean(SHAREDPREFERENCES_LOGIN_SAVE,true)
            saveDataEditor.apply()
        }
        else{
            saveDataEditor.putString(SHAREDPREFERENCES_LOGIN_EMAIL,"")
            saveDataEditor.putString(SHAREDPREFERENCES_LOGIN_PASSWORD,"")
            saveDataEditor.putBoolean(SHAREDPREFERENCES_LOGIN_SAVE,false)
                .apply()
        }
    }

    fun restoreSharedPreferences(){
        sharedPreferences = requireActivity().getSharedPreferences(SHAREDPREFERENCES_LOGIN,MODE_PRIVATE)
        isChecked = sharedPreferences.getBoolean(SHAREDPREFERENCES_LOGIN_SAVE,false)

        with(sharedPreferences) {
            if (isChecked) {
                binding.user!!.email = getString(SHAREDPREFERENCES_LOGIN_EMAIL, "").toString()
                binding.user!!.password = getString(SHAREDPREFERENCES_LOGIN_PASSWORD, "").toString()
                binding.saveLogin.isChecked = isChecked
            }
        }
    }

    fun invalidateAll(){
        binding.loginEditText.setText("")
        binding.passwordEditText.setText("")
    }
}