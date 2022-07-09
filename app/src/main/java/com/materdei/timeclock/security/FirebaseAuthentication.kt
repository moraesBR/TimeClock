package com.materdei.timeclock.security

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.materdei.timeclock.dto.AuthState
import com.materdei.timeclock.dto.User

object FirebaseAuthentication {

    var firebaseUser: FirebaseUser? = null
    var authState: AuthState = AuthState.Idle

    fun handleSignIn(user: User, action: (Boolean) -> Unit){
        if (!isValidEmail(user.email)) {
            authState = AuthState.AuthError("Invalid email")
            firebaseUser = null
            action(false)
            return
        }

        if(user.password.isBlank()) {
            authState = AuthState.AuthError("Please, inform your password")
            firebaseUser = null
            action(false)
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseUser = FirebaseAuth.getInstance().currentUser
                    authState = AuthState.Sucess
                    action(true)
                } else {
                    task.exception?.let {
                        firebaseUser = null
                        authState = AuthState.AuthError(it.localizedMessage)
                        action(false)
                    }
                }
            }
    }

    private fun isValidEmail(email: CharSequence?): Boolean =
        !email.isNullOrBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}