package com.materdei.timeclock.security

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.materdei.timeclock.dto.User

object FirebaseAuthentication {
    private lateinit var userAuth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    fun connect(){
        userAuth = FirebaseAuth.getInstance()
        currentUser = userAuth.currentUser
        currentUser?.reload()
    }

    fun performAuthetication(user: User, action: (Boolean) -> Unit){
        if(!user.email.isBlank() && !user.password.isBlank()) {
            userAuth.signInWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //val user = userAuth.currentUser
                        action(true)
                    } else {
                        action(false)
                    }
                }
        } else action(false)
    }

    fun disconnect(){
        userAuth.signOut()
    }

}