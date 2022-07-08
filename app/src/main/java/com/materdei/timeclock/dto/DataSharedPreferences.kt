package com.materdei.timeclock.dto

sealed class DataSharedPreferences{
    class AuthorizationPreferences: DataSharedPreferences()
    class HomePrefererences : DataSharedPreferences()
    class MainPreferences(val user: User, val isChecked: Boolean) : DataSharedPreferences()
}