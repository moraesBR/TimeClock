package com.materdei.timeclock.viewmodels

import androidx.appcompat.app.AppCompatActivity

interface PermissionsHandler {
    fun checkHasPermission(activity: AppCompatActivity, permissions: Array<String>): Boolean
    fun requestPermission(activity: AppCompatActivity, permissions: Array<out String>, requestCode: Int )
    fun checkLocationEnabled(activity: AppCompatActivity): Boolean
    fun requestLocation(activity: AppCompatActivity)
}

