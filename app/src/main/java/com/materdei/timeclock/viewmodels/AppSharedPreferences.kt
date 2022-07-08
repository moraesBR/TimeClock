package com.materdei.timeclock.viewmodels

import android.content.Context
import android.content.SharedPreferences
import com.materdei.timeclock.dto.DataSharedPreferences
import com.materdei.timeclock.dto.FragmentsID
import com.materdei.timeclock.dto.User
import com.materdei.timeclock.utils.Constants

object AppSharedPreferences {

    fun save(context: Context, fragmentsID: FragmentsID, data: DataSharedPreferences){
        val editor = context
            .getSharedPreferences(Constants.SHAREDPREFERENCES, Context.MODE_PRIVATE).edit()

        when(fragmentsID){
            FragmentsID.AUTHORIZATION -> saveAuthorizationPreferences(editor,data)
            FragmentsID.HOME -> saveHomePreferences(editor,data)
            FragmentsID.MAIN -> saveMainPreferences(editor,data)
        }
    }

    private fun saveAuthorizationPreferences(editor: SharedPreferences.Editor,
                                             data: DataSharedPreferences){

    }

    private fun saveHomePreferences(editor: SharedPreferences.Editor, data: DataSharedPreferences){

    }

    private fun saveMainPreferences(editor: SharedPreferences.Editor, data: DataSharedPreferences){
        val mainPreferences = data as DataSharedPreferences.MainPreferences

        editor.apply {
            putString(Constants.SHAREDPREFERENCES_LOGIN_EMAIL, mainPreferences.user.email)
            putString(Constants.SHAREDPREFERENCES_LOGIN_PASSWORD, mainPreferences.user.password)
            putBoolean(Constants.SHAREDPREFERENCES_LOGIN_SAVE, mainPreferences.isChecked)
        }.apply()

    }

    fun restore(context: Context, fragmentsID: FragmentsID): DataSharedPreferences{
        val sharedPreferences = context
            .getSharedPreferences(Constants.SHAREDPREFERENCES, Context.MODE_PRIVATE)

        return when(fragmentsID){
            FragmentsID.AUTHORIZATION -> loadAuthorizationPreferences(sharedPreferences)
            FragmentsID.HOME -> loadHomePreferences(sharedPreferences)
            FragmentsID.MAIN -> loadMainPreferences(sharedPreferences)
        }
    }

    private fun loadAuthorizationPreferences(sharedPreferences: SharedPreferences):
            DataSharedPreferences.AuthorizationPreferences {
        return DataSharedPreferences.AuthorizationPreferences()
    }

    private fun loadHomePreferences(sharedPreferences: SharedPreferences):
            DataSharedPreferences.HomePrefererences {
        return DataSharedPreferences.HomePrefererences()
    }

    private fun loadMainPreferences(sharedPreferences: SharedPreferences):
            DataSharedPreferences.MainPreferences {

        with(sharedPreferences) {
            val user = User(getString(Constants.SHAREDPREFERENCES_LOGIN_EMAIL, "").toString(),
                            getString(Constants.SHAREDPREFERENCES_LOGIN_PASSWORD, "").toString())
            val isChecked = getBoolean(Constants.SHAREDPREFERENCES_LOGIN_SAVE,false)

            return DataSharedPreferences.MainPreferences(user,isChecked)
        }

    }

}