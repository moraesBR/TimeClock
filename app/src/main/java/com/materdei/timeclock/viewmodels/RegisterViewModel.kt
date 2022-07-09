package com.materdei.timeclock.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.materdei.timeclock.dto.RegisterDetails
import com.materdei.timeclock.security.FirebaseAuthentication
import com.materdei.timeclock.utils.Constants.Companion.REGISTERVIEWMODEL_REGISTERS_COLLECTION
import com.materdei.timeclock.utils.Constants.Companion.REGISTERVIEWMODEL_ROOT_COLLECTION

class RegisterViewModel: ViewModel() {

    private var _registers = MutableLiveData<MutableList<RegisterDetails>>()
    val dbRegisters: LiveData<MutableList<RegisterDetails>>
        get() = _registers

    init {
        _registers.value = mutableListOf()
    }

    fun getRegisters() {
        FirebaseAuthentication.firebaseUser?.let {
            val collectionPath = "${REGISTERVIEWMODEL_ROOT_COLLECTION}/${it.uid}"
            val document = Firebase.firestore.document(collectionPath)
            document.collection(REGISTERVIEWMODEL_REGISTERS_COLLECTION)
                .get()
                .addOnSuccessListener { results ->
                    _registers.value = results.toObjects(RegisterDetails::class.java).toMutableList()
                }
        }
    }

    fun newRegisters(){
        FirebaseAuthentication.firebaseUser?.let {
            val collectionPath = "${REGISTERVIEWMODEL_ROOT_COLLECTION}/${it.uid}"
            val document = Firebase.firestore.document(collectionPath)
            document.addSnapshotListener{ snapshot, e ->
                if (e != null) {
                    Log.w("FIRESTORE", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(RegisterDetails::class.java)
                        ?.let { data -> _registers.value!!.add(data) }
                }
            }
        }
    }

    fun putRegister(register: RegisterDetails){
        FirebaseAuthentication.firebaseUser?.let {
            val db = Firebase.firestore.collection(REGISTERVIEWMODEL_ROOT_COLLECTION)
            val dataUser = hashMapOf(
                "email" to it.email,
                "username" to it.email?.substringBefore("@"),
                "name" to it.displayName,
                "phonenumber" to it.phoneNumber
            )

            val collectionPath = REGISTERVIEWMODEL_ROOT_COLLECTION +
                    "/${it.uid}/${REGISTERVIEWMODEL_REGISTERS_COLLECTION}"

            db.document(it.uid).set(dataUser).addOnSuccessListener {
                val dataPunch = hashMapOf(
                    "date" to register.date,
                    "time" to register.time,
                    "punch" to register.punch
                )
                Firebase.firestore.collection(collectionPath).document(register.getKey()).set(dataPunch)
                    .addOnSuccessListener {
                        _registers.value!!.add(register)
                    }
            }
        }
    }
}