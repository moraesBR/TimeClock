package com.materdei.timeclock.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DoItViewModel: ViewModel() {
    private val _canDoIt = MutableLiveData<Boolean>(false)
    val canDoIt: LiveData<Boolean>
        get() = _canDoIt

    fun engage(){
        _canDoIt.value = true
    }

    fun disengage(){
        _canDoIt.value = false
    }
}