package com.materdei.timeclock.dto

sealed class AuthState {
    object Idle: AuthState()
    object Loading: AuthState()
    object Sucess: AuthState()
    class AuthError(val msg: String? = null): AuthState()
}
