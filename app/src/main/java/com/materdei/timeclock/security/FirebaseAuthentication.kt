package com.materdei.timeclock.security

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.materdei.timeclock.dto.AuthState
import com.materdei.timeclock.dto.User

class FirebaseAuthentication: ViewModel() {

    private var _authState: AuthState = AuthState.Idle
    val authState: AuthState
        get() = _authState

     fun handleSignIn(user: User, action: (Boolean) -> Unit){
        if (!isValidEmail(user.email)) {
            _authState = AuthState.AuthError("Invalid email")
            action(false)
            return
        }

        if(user.password.isBlank()) {
            _authState = AuthState.AuthError("Please, inform your password")
            action(false)
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //_userFirebase.value = FirebaseAuth.getInstance().currentUser
                    _authState = AuthState.Sucess
                    action(true)
                } else {
                    task.exception?.let {
                        _authState = AuthState.AuthError(it.localizedMessage)
                        action(false)
                    }
                }
            }
    }

    private fun isValidEmail(email: CharSequence?): Boolean =
        !email.isNullOrBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}